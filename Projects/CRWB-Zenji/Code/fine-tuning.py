
# coding: utf-8

# # Fine-tuning 
# 
# This notebook will be used to build a model to classifiy dishes with a pre-trained [ImageNet](http://www.image-net.org/) model from the MXNet [model zoo](http://data.mxnet.io/models/). 
# 
# All of the network’s weights will be updated and also replaced in the last fully-connected layer with the new number of output classes by a smaller learning rate. For more in depth reading on fine-tuning with MXNet check this [tutorial](http://mxnet.io/how_to/finetune.html) and for more details on the how CNN's work check out [CS231n course](http://cs231n.github.io/convolutional-networks/#overview) and [MNIST example](https://github.com/dmlc/mxnet-notebooks/blob/master/python/tutorials/mnist.ipynb) with MXNet.
# 

# # Pre-processing the dataset
# 
# 1. Download the data.
# 
# 2. Create two folders "train" and "valid".
# 
# 3. Create additonal directories to get a directory structure as shown below:
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
# 
# 4. Move all data into train. 
# 
# 5. Move a percentage of the data in to the validation directory to create the validation set. 
# 
# ```
#     import os
#     import random
#     import shutil
#     
#     # helper functions
# 
#     # download function
#     import os, urllib
#     def download(url):
#         filename = url.split("/")[-1]
#         if not os.path.exists(filename):
#             urllib.urlretrieve(url, filename)
# 
#     category_directory = 'train/category'
# 
#     all_objects = os.listdir(category_directory)
#    
#     # 20%
#     p = 20.0
#     
#     N = int(len(all_objects)/p)
# 
#     for f in random.sample(all_objects, N):
#        shutil.move(category_directory + "/" + f, "validation/category/" + f)
# 
# ```
# 
# 6. Create a list for training and validation set
# ```
#  python ~/mxnet/tools/im2rec.py --list True --recursive True data_train.lst data/train
# 
#  python ~/mxnet/tools/im2rec.py --list True --recursive True data_validation.lst data/validation
# ```
# 
# 7. Convert the images in to MXNet RecordIO format
# ```
#  python ~/mxnet/tools/im2rec.py --resize 224 --quality 90 --num-thread 16 data_train.lst data/train
# 
#  python ~/mxnet/tools/im2rec.py --resize 224 --quality 90 --num-thread 16 data_validation.lst data/validation
# ```  
# 
# The data_train.rec and data_validation.rec files should be created.

# # CODE
# 
# The function below returns the data iterators.

# In[9]:


#Data Iterators for cats vs dogs dataset

import mxnet as mx

def get_iterators(batch_size, data_shape=(3, 224, 224)):
    train = mx.io.ImageRecordIter(
        path_imgrec         = './data_train.rec', 
        data_name           = 'data',
        label_name          = 'softmax_label',
        batch_size          = batch_size,
        data_shape          = data_shape,
        shuffle             = True,
        rand_crop           = True,
        rand_mirror         = True)
    val = mx.io.ImageRecordIter(
        path_imgrec         = './data_validation.rec',
        data_name           = 'data',
        label_name          = 'softmax_label',
        batch_size          = batch_size,
        data_shape          = data_shape,
        rand_crop           = False,
        rand_mirror         = False)
    return (train, val)


# ## Dowload pre-trained model from the model zoo (ResidualNet152)
# 
# Download a pre-trained 152-layer ResNet model and load into memory.
# 
#     Note: If load_checkpoint reports error the downloaded files need to be removed before to try get the model again.

# In[17]:


# helper functions

import os, urllib
def download(url):
    filename = url.split("/")[-1]
    if not os.path.exists(filename):
        urllib.urlretrieve(url, filename)
        
def get_model(prefix, epoch):
    download(prefix+'-symbol.json')
    download(prefix+'-%04d.params' % (epoch,))

get_model('http://data.mxnet.io/models/imagenet/resnet/152-layers/resnet-152', 0)
sym, arg_params, aux_params = mx.model.load_checkpoint('resnet-152', 0)


# ## Fine tuning the model
# 
# 
# To fine-tune a network, the last fully-connected layer with must be replace by a new one that outputs the desired number of classes. The weights are initialize randomly. Then training will continue normaly. Sometimes it’s common use a smaller learning rate based on the intuition that good result may already be reached.
# 
# First of all, a function which replaces the the last fully-connected layer for a given network needs to be defined.

# In[6]:


def get_fine_tune_model(symbol, arg_params, num_classes, layer_name='flatten0'):
    """
    symbol: the pre-trained network symbol
    arg_params: the argument parameters of the pre-trained model
    num_classes: the number of classes for the fine-tune datasets
    layer_name: the layer name before the last fully-connected layer
    """
    all_layers = sym.get_internals()
    net = all_layers[layer_name+'_output']
    net = mx.symbol.FullyConnected(data=net, num_hidden=num_classes, name='fc1')
    net = mx.symbol.SoftmaxOutput(data=net, name='softmax')
    new_args = dict({k:arg_params[k] for k in arg_params if 'fc1' not in k})
    return (net, new_args)


# ## Training the model
# 
# A fit function that creates an MXNet module instance needs to be defined to bind the data and symbols. 
# 
# init_params is called to randomly initialize parameters
# 
# set_params is called to replace all parameters except for the last fully-connected layer with pre-trained model.
# 
# #### Note: change mx.gpu to mx.cpu to run training on CPU (much slower)

# In[10]:


import logging
head = '%(asctime)-15s %(message)s'
logging.basicConfig(level=logging.DEBUG, format=head)

def fit(symbol, arg_params, aux_params, train, val, batch_size, num_gpus=1, num_epoch=1):
    devs = [mx.gpu(i) for i in range(num_gpus)] # replace mx.gpu by mx.cpu for CPU training
    mod = mx.mod.Module(symbol=new_sym, context=devs)
    mod.bind(data_shapes=train.provide_data, label_shapes=train.provide_label)
    mod.init_params(initializer=mx.init.Xavier(rnd_type='gaussian', factor_type="in", magnitude=2))
    mod.set_params(new_args, aux_params, allow_missing=True)
    mod.fit(train, val, 
        num_epoch=num_epoch,
        batch_end_callback = mx.callback.Speedometer(batch_size, 10),        
        kvstore='device',
        optimizer='sgd',
        optimizer_params={'learning_rate':0.009},
        eval_metric='acc')
    
    return mod


# At this point the helper functions are setup and training can to start.
# Its recommended that to train on a GPU instance, preferably p2.* family. For this notebook an AWS EC2 p2.xlarge, which has one NVIDIA K80 GPU, was considered.

# In[ ]:


num_classes = i # Number of categories
batch_per_gpu = 4
num_gpus = 1
(new_sym, new_args) = get_fine_tune_model(sym, arg_params, num_classes)

batch_size = batch_per_gpu * num_gpus
(train, val) = get_iterators(batch_size)
mod = fit(new_sym, new_args, aux_params, train, val, batch_size, num_gpus)
metric = mx.metric.Accuracy()
mod_score = mod.score(val, metric)
print mod_score


# Finally, save the newly trained model 

# In[11]:


prefix = 'resnet-mxnet-name'
epoch = 1
mc = mod.save_checkpoint(prefix, epoch)


# ## Loading saved model

# In[14]:


# load the model
import cv2
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

model_symbol = "resnet-mxnet-name-symbol.json"
model_params = "resnet-mxnet-param-000i.params"
sym, arg_params, aux_params = load_model(model_symbol, model_params)
mod = mx.mod.Module(symbol=sym)

# bind the model, set training to False and define the data shape
mod.bind(for_training=False, data_shapes=dshape)
mod.set_params(arg_params, aux_params)


# ## Generate predictions for an arbitrary image

# In[16]:


import urllib2
from collections import namedtuple
Batch = namedtuple('Batch', ['data'])

def preprocess_image(img, show_img=False):
    '''
    convert the image to a numpy array
    '''
    img = cv2.resize(img, (224, 224))
    img = np.swapaxes(img, 0, 2)
    img = np.swapaxes(img, 1, 2) 
    img = img[np.newaxis, :] 
    return img

url = ''
req = urllib2.urlopen(url)

image = np.asarray(bytearray(req.read()), dtype="uint8")
image = cv2.imdecode(image, cv2.IMREAD_COLOR)
img = preprocess_image(image)

mod.forward(Batch([mx.nd.array(img)]))

# predict
prob = mod.get_outputs()[0].asnumpy()
print prob


# ### Inspecting incorrect labels

# In[ ]:


# generate predictions for entire validation dataset
import os
import cv2

path = 'data/valid/category/' # change as needed
files = [path + f for f in os.listdir(path)]
incorrect_labels = []

# incorrect category labels
for f in files:
    img = cv2.imread(f)
    img = preprocess_image(img)
    mod.forward(Batch([mx.nd.array(img)]))
    prob = mod.get_outputs()[0].asnumpy()
    if prob.argmax() != 1: # not a cat
        print f
        incorrect_labels.append(f)


# In[ ]:


from matplotlib import pyplot as plt
get_ipython().magic(u'matplotlib inline')
import numpy as np

# plot helper
def plots(ims, figsize=(12,6), rows=1, interp=False, titles=None):
    if type(ims[0]) is np.ndarray:
        ims = np.array(ims).astype(np.uint8)
        if (ims.shape[-1] != 3):
            ims = ims.transpose((0,2,3,1))
    f = plt.figure(figsize=figsize)
    for i in range(len(ims)):
        sp = f.add_subplot(rows, len(ims)//rows, i+1)
        sp.axis('Off')
        if titles is not None:
            sp.set_title(titles[i], fontsize=16)
        plt.imshow(ims[i], interpolation=None if interp else 'none')

# individual plot of incorrect label
img_path = incorrect_labels[0]
plots([cv2.imread(img_path)])
plt.show()

