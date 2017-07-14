
# coding: utf-8

# # Pre-processing the dataset

# # Fine-tuning unsig the UEC FOOD-256 
# 
# This notebook will be used to build a model to classifiy dishes with a pre-trained [ImageNet](http://www.image-net.org/) model from the MXNet [model zoo](http://data.mxnet.io/models/) and the  [UEC FOOD 256](http://foodcam.mobi/dataset256.zip) dataset. 
# 
# All of the network’s weights will be updated and also replaced in the last fully-connected layer with the new number of output classes by a smaller learning rate. For more in depth reading on fine-tuning with MXNet check this [tutorial](http://mxnet.io/how_to/finetune.html) and for more details on the how CNN's work check out [CS231n course](http://cs231n.github.io/convolutional-networks/#overview) and [MNIST example](https://github.com/dmlc/mxnet-notebooks/blob/master/python/tutorials/mnist.ipynb) with MXNet.
# 

# In[10]:


# helper functions

# download function
import os, urllib

def download(url, location):
    filename = url.split("/")[-1]
    document = os.path.join(location, filename)
    if not os.path.exists(document):
        urllib.urlretrieve(url, document)
        


# 1.- Set the root path and the temp path

# In[2]:


root = "/media/"
temp = os.path.join(root, "sf_Temp/UECFOOD-256")


# 2.- Set the dataset path

# 2.1.- Download the dataset [UEC FOOD 256](http://foodcam.mobi/dataset256.zip)

# In[3]:


#download("http://foodcam.mobi/dataset256.zip", temp)

#dataset = os.path.join(root, "sf_Datasets/UECFOOD-256.zip")


# 2.2.- Extract the dataset

# In[4]:


#import zipfile

#archiver = zipfile.ZipFile(dataset, 'r')
#archiver.extractall(temp)
#archiver.close()


# In[5]:


dataset = os.path.join(root, "sf_Datasets/UECFOOD-256")


# 3.- Create two folders: "train" and "validation"

# In[7]:


import shutil

train = os.path.join(temp, "train")

if os.path.exists(train):
    shutil.rmtree(train)
    
os.makedirs(train)
    
validation = os.path.join(temp, "validation")

if os.path.exists(validation):
    shutil.rmtree(validation)

os.makedirs(validation)


# 3.1- Create additonal directories to get a directory structure as shown below:
# 
# ```
#     train/
# 
#     ├── 0category
#     ├── ..category
#     └── icategory
# 
#     validation/
# 
#     ├── 0category
#     ├── ..category
#     └── icategory
# ```

# 4.- Move all data into train. 

# In[7]:


import cv2, imghdr
import pandas as pd

#metafile = "category.txt" 

#path = os.path.join(dataset, metafile)
#if os.path.isfile(path):
    #shutil.copy2(path, os.path.join(train, metafile))

metafile = "bb_info.txt"  

for category, categories, files in os.walk(dataset):
    path = os.path.join(category, metafile)
    df = None
    if os.path.isfile(path):
        df = pd.read_csv(os.path.join(category, metafile), delim_whitespace = True, index_col = 0)
        #shutil.copy2(path, os.path.join(category, metafile))
    for document in files:
        path = os.path.join(category, document)
        if imghdr.what(path) is not None:
            image = cv2.imread(os.path.join(category, document))
            name = os.path.splitext(document)[0]
            boxes = df.loc[[int(name)]]
            for i, (index, box) in enumerate(boxes.iterrows()): 
                x1 = box.loc["x1"]
                x2 = box.loc["x2"]
                y1 = box.loc["y1"]
                y2 = box.loc["y2"]
                cropped = image[y1:y2, x1:x2];
                cv2.imwrite(os.path.join(train, os.path.basename(category), str(i) + document), cropped)
        #else:
            #shutil.copy2(path, os.path.join(train, os.path.basename(category)))
    for directory in categories:
        path = os.path.join(train, directory)
        if os.path.exists(path):
            shutil.rmtree(path)
        os.makedirs(path)


# 5.- Move a percentage of the data in to the validation directory to create the validation set

# In[8]:


import random

# 20%
percentage = 20.0
      
for category, categories, files in os.walk(train):
    size = int(len(files) / percentage)
    for name in random.sample(files, size):
        if imghdr.what(os.path.join(train, category, name)) is not None:
            shutil.move(os.path.join(category, name), os.path.join(validation, os.path.basename(category)))
    for name in categories:
        directory = os.path.join(validation, name)
        if not os.path.exists(directory):
            os.makedirs(directory)


# 6.- Create a list for training and validation set

# In[9]:


import mxnet as mx
    
im2rec = os.path.join(os.path.dirname(mx.__file__), "tools/im2rec.py")

import subprocess

trainning_list = os.path.join(train, "train")

if os.path.isfile(im2rec):
    if os.path.exists(train):
        subprocess.call(["python", im2rec, "--list", "True", "--recursive","True", trainning_list, train])

validation_list = os.path.join(validation, "validation")        
        
if os.path.isfile(im2rec):
    if os.path.exists(validation):
        subprocess.call(["python", im2rec, "--list","True", "--recursive", "True", validation_list, validation])


# 7.- Convert the images in to MXNet RecordIO format

# In[10]:


if os.path.isfile(im2rec):
    if os.path.exists(train):
        subprocess.call(["python", im2rec, "--resize", "224", "--quality","90", "--num-thread", "16", trainning_list, train])

if os.path.isfile(im2rec):
    if os.path.exists(validation):
        subprocess.call(["python", im2rec, "--resize", "224", "--quality", "90", "--num-thread", "16", validation_list, validation])


# The data_train.rec and data_validation.rec files should be created.

# In[12]:


for directory, directories, files in os.walk(train):
    for document in files:
        path = os.path.join(directory, document)
        if imghdr.what(path) is None:
            print path
            
for directory, directories, files in os.walk(validation):
    for document in files:
        path = os.path.join(directory, document)
        if imghdr.what(path) is None:
            print path


# # CODE
# 
# The function below returns the data iterators.

# In[8]:


#Data Iterators for cats vs dogs dataset

def get_iterators(batch_size, data_shape=(3, 224, 224)):
    trainning_iterator = mx.io.ImageRecordIter(
        path_imgrec         = os.path.join(train, "train.rec"), 
        data_name           = "data",
        label_name          = "softmax_label",
        batch_size          = batch_size,
        data_shape          = data_shape,
        shuffle             = True,
        rand_crop           = True,
        rand_mirror         = True)
    validation_iterator = mx.io.ImageRecordIter(
        path_imgrec         = os.path.join(validation, "validation.rec"),
        data_name           = "data",
        label_name          = "softmax_label",
        batch_size          = batch_size,
        data_shape          = data_shape,
        rand_crop           = False,
        rand_mirror         = False)
    return (trainning_iterator, validation_iterator)


# ## Dowload pre-trained model from the model zoo (ResidualNet152)
# 
# Download a pre-trained 152-layer ResNet model and load into memory.
# 
#     Note: If load_checkpoint reports error the downloaded files need to be removed before to try get the model again.

# In[15]:


# download function
def get_model(prefix, epoch, location):
    download(prefix + "-symbol.json", location)
    download(prefix + "-%04d.params" % (epoch,), location)

get_model("http://data.mxnet.io/models/imagenet/resnet/152-layers/resnet-152", 0, temp)

symbol, arg_params, aux_params = mx.model.load_checkpoint(os.path.join(temp, "resnet-152"), 0)


# ## Fine tuning the model
# 
# 
# To fine-tune a network, the last fully-connected layer with must be replace by a new one that outputs the desired number of classes. The weights are initialize randomly. Then training will continue normaly. Sometimes it’s common use a smaller learning rate based on the intuition that good result may already be reached.
# 
# First of all, a function which replaces the the last fully-connected layer for a given network needs to be defined.

# In[16]:


def get_fine_tune_model(sym, arg_params, num_classes, layer_name = "flatten0"):
    """
    symbol: the pre-trained network symbol
    arg_params: the argument parameters of the pre-trained model
    num_classes: the number of classes for the fine-tune datasets
    layer_name: the layer name before the last fully-connected layer
    """
    all_layers = sym.get_internals()
    net = all_layers[layer_name + "_output"]
    net = mx.symbol.FullyConnected(data = net, num_hidden = num_classes, name = "fc1")
    net = mx.symbol.SoftmaxOutput(data = net, name = "softmax")
    new_args = dict({k:arg_params[k] for k in arg_params if "fc1" not in k})
    return (net, new_args)


# ## Training the model
# 
# A fit function that creates an MXNet module instance needs to be defined to bind the data and symbols. 
# 
# init_params is called to randomly initialize parameters
# 
# set_params is called to replace all parameters except for the last fully-connected layer with pre-trained model.
# 
# #### Note: change mx.cpu to mx.gpu to run training on GPU (much faster)

# In[17]:


import logging

logging.basicConfig(level = logging.DEBUG, format = "%(asctime)-15s %(message)s")

def fit(sym, arg_params, aux_params, train_iter, val_iter, batch_size, num_pus = 1, num_epoch = 1):
    devs = [mx.cpu(i) for i in range(num_pus)] # replace mx.cpu by mx.gpu for GPU training
    mod = mx.mod.Module(symbol = new_sym, context = devs)
    mod.bind(data_shapes = train_iter.provide_data, label_shapes = train_iter.provide_label)
    #mod.init_params(initializer = mx.init.Xavier(rnd_type = "gaussian", factor_type = "in", magnitude = 2))
    #mod.set_params(new_args, aux_params, allow_missing = True)
    mod.fit(
        train_iter, 
        val_iter, 
        num_epoch = num_epoch,
        arg_params = arg_params,
        aux_params = aux_params,
        allow_missing = True,
        batch_end_callback = mx.callback.Speedometer(batch_size, 10),        
        kvstore = "device",
        optimizer = "sgd",
        optimizer_params = {"learning_rate":0.01},
        initializer = mx.init.Xavier(rnd_type = "gaussian", factor_type = "in", magnitude = 2),
        eval_metric ='acc'
    )
    return mod


# At this point the helper functions are setup and training can to start.
# Its recommended that to train on a GPU instance, preferably p2.* family. For this notebook an AWS EC2 p2.xlarge, which has one NVIDIA K80 GPU, was considered.

# In[19]:


num_classes = 256 # Number of categories

batch_per_pu = 16
num_pus = 1

(new_sym, new_args) = get_fine_tune_model(symbol, arg_params, num_classes)

batch_size = batch_per_pu * num_pus

(train_iter, val_iter) = get_iterators(batch_size)

mod = fit(new_sym, new_args, aux_params, train_iter, val_iter, batch_size, num_pus)

metric = mx.metric.Accuracy()

print mod.score(val_iter, metric)


# Finally, save the newly trained model 

# In[ ]:


prefix = "resnet-mxnet-dishes"
epoch = 1

mc = mod.save_checkpoint(prefix, epoch)

for document in os.listdir("./"):
    if (document.startswith(prefix)):
        shutil.move(document, temp)


# ## Loading saved model

# In[ ]:


# load the model

dshape = [('data', (1,3,224,224))]

def load_model(s_fname, p_fname):
    """
    load model checkpoint from file.
    :return: (arg_params, aux_params)
    arg_params : dict of str to NDArray
        Model parameter, dict of name to NDArray of net's weights.
    aux_params : dict of str to NDArray
        Model parameter, dict of name to NDArray of net's auxiliary states.
    """
    symbol = mx.symbol.load(s_fname)
    save_dict = mx.nd.load(p_fname)
    arg_params = {}
    aux_params = {}
    for k, v in save_dict.items():
        tp, name = k.split(':', 1)
        if tp == 'arg':
            arg_params[name] = v
        if tp == 'aux':
            aux_params[name] = v
    return symbol, arg_params, aux_params
       
model_symbol = None
model_params = None

for document in os.listdir(temp):
    filename, extension = os.path.splitext(document)
    if (filename.startswith(prefix)):
        if(extension == "json"):
            model_symbol = document
        if(extension == "params"):
            model_params = document

symbol, arg_params, aux_params = load_model(model_symbol, model_params)
mod = mx.mod.Module(symbol = symbol)

# bind the model, set training to False and define the data shape
mod.bind(for_training = False, data_shapes = dshape)
mod.set_params(arg_params, aux_params)


# ## Generate predictions for an arbitrary image

# In[ ]:


import urllib2

from collections import namedtuple

Batch = namedtuple('Batch', ['data'])

def preprocess_image(img, show = False):
    '''
    convert the image to a numpy array
    '''
    img = cv2.resize(img, (224, 224))
    img = np.swapaxes(img, 0, 2)
    img = np.swapaxes(img, 1, 2) 
    img = img[np.newaxis, :] 
    return img

url = 'https://cdn.pixabay.com/photo/2016/03/05/19/02/abstract-1238248_640.jpg'
request = urllib2.urlopen(url)

image = np.asarray(bytearray(req.read()), dtype = "uint8")
image = cv2.imdecode(image, cv2.IMREAD_COLOR)
image = preprocess_image(image)

mod.forward(Batch([mx.nd.array(image)]))

# predict
prob = mod.get_outputs()[0].asnumpy()
print prob


# ### Inspecting incorrect labels
