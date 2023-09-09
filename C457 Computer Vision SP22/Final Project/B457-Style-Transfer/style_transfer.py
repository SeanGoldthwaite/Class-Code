import argparse, os
import os
import tensorflow as tf
import vgg_load
from PIL import Image
import numpy as np

# Turn off warning that tf is using more than 10% of memory
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'

tf.compat.v1.disable_v2_behavior()

image_dir = os.path.join(os.path.dirname(__file__), 'images')
output_dir = os.path.join(os.path.dirname(__file__), 'output')
checkpoint_dir = os.path.join(os.path.dirname(__file__), 'checkpoints')

if not os.path.isdir(image_dir):
    os.makedirs(image_dir)
if not os.path.isdir(output_dir):
    os.makedirs(output_dir)
if not os.path.isdir(checkpoint_dir):
    os.makedirs(checkpoint_dir)

# Hyper-parameters

# Which vgg layers to use for reconstruction
content_layers = ['conv3_1', 'conv4_1', 'conv5_1']
style_layers = ['conv4_1', 'conv4_3', 'conv5_1', 'conv5_3']

# Content loss weight
alpha = 3e-4
# Style loss weight
beta = 1e0

# Ratio of noise-to-content_image for the starting image
noise_ratio = 0.80

# Learning rate for optimizer
learning_rate = 0.4

# Max iterations for optimizer
max_iter = 1000
print_interval = 50
save_interval = 100
verbose = True

def main():
    parser = argparse.ArgumentParser(description='Neural Style Transfer')
    parser.add_argument('content_image', type=str, help='Path to content image')
    parser.add_argument('style_image', type=str, help='Path to style image')
    args = parser.parse_args()

    if args.content_image[-3:] != 'jpg':
        content_image = Image.open(os.path.join(image_dir, args.content_image + '.jpg'))
        content_image_name = args.content_image
    else:
        content_image = Image.open(os.path.join(image_dir, args.content_image))
        content_image_name = args.content_image[:-4]

    if args.style_image[-3:] != 'jpg':
        style_image = Image.open(os.path.join(image_dir, args.style_image + '.jpg'))
        style_image_name = args.style_image
    else:
        style_image = Image.open(os.path.join(image_dir, args.style_image))
        style_image_name = args.style_image[:-4]

    cw, ch = content_image.size
    style_image = style_image.resize((cw, ch), Image.BICUBIC)

    content_arr = np.array(content_image)
    style_arr = np.array(style_image)

    post_arr = stylize(content_arr, style_arr)
    post_img = Image.fromarray(post_arr)

    filename = content_image_name + ' with ' + style_image_name + '.jpg'
    save_final_image(post_arr, filename)

def stylize(content_arr, style_arr):
    content_arr = preprocess(content_arr)
    style_arr = preprocess(style_arr)

    mean_pix = np.mean(np.mean(np.mean(style_arr, axis=2), axis=1)[0])
    std_pix = np.mean(np.std(np.std(style_arr, axis=2), axis=1)[0])
    noise_img = np.random.normal(loc=mean_pix, scale=2*std_pix, size=content_arr.shape).astype(np.float16)
    initial_image = noise_ratio * noise_img + (1 - noise_ratio) * content_arr

    graph = tf.Graph()
    with graph.as_default():
        vgg = vgg_load.load_vgg(content_arr)

        with tf.compat.v1.Session() as session:
            L_content = total_content_loss(session, vgg, content_arr)
            L_style = total_style_loss(session, vgg, style_arr)

            L_total = alpha * L_content + beta * L_style

            optimizer = tf.compat.v1.train.AdamOptimizer(learning_rate)

            train = optimizer.minimize(L_total)
            init = tf.compat.v1.global_variables_initializer()

            session.run(init)
            session.run(vgg['input'].assign(initial_image))

            for iter in range(max_iter+1):
                session.run(train)
                if iter % print_interval == 0:
                    if verbose:
                        print(f'Iteration: {iter}/{max_iter}\tLoss: {L_total.eval()}\tSaved Image: {iter % save_interval == 0}')
                    if iter % save_interval == 0:
                        save_checkpoint(session, vgg, iter)

            output = session.run(vgg['input'])

            post_img = postprocess(output)
              
    return post_img

def style_loss(style_img, generated_img):
    a, h, w, c = style_img.get_shape()

    m = h.value * w.value
    n = c.value

    A = gram_matrix(style_img, m, n)
    G = gram_matrix(generated_img, m, n)

    loss = (1 / (4 * m**2 * n**2)) * tf.reduce_sum(tf.pow((G - A), 2))

    return loss

def total_style_loss(session, vgg, style_img):
    style_layer_weights = [1 / len(style_layers) for layer in style_layers]
    loss = 0

    session.run(vgg['input'].assign(style_img))

    for layer, weight in zip(style_layers, style_layer_weights):
        a = session.run(vgg[layer])
        x = vgg[layer]

        a = tf.convert_to_tensor(a)

        loss += style_loss(a, x)

    return loss / len(style_layers)

def content_loss(content_img, generated_img):
    a, h, w, c = content_img.get_shape()

    m = h.value * w.value
    n = c.value

    loss = 0.5 * tf.reduce_sum(tf.pow((generated_img - content_img), 2))

    return loss

def total_content_loss(session, vgg, content_img):
    content_layer_weights = [1 / len(content_layers) for layer in content_layers]
    loss = 0

    session.run(vgg['input'].assign(content_img))

    for layer, weight in zip(content_layers, content_layer_weights):
        p = session.run(vgg[layer])
        x = vgg[layer]

        p = tf.convert_to_tensor(p)

        loss += content_loss(p, x)

    return loss / len(content_layers)

def gram_matrix(mat, h, w):
    mat = tf.reshape(mat, (h, w))
    return tf.matmul(tf.transpose(mat), mat)

# Tensorflow takes a minimum of 4 dimensions we have to add one
def preprocess(image_arr):
    image_arr = image_arr[np.newaxis,:,:,:]
    return image_arr

def postprocess(image_arr):
    #remove extra dim
    image_arr = image_arr[0]

    #convert to uint8 so PIL can handle it
    image_arr = np.clip(image_arr, 0, 255).astype(np.uint8)
    
    return image_arr


def save_checkpoint(session, vgg, iter):
    out = session.run(vgg['input'])
    out = postprocess(out)
    filename = 'checkpoint (' + str(iter) + ').jpg'
    img = Image.fromarray(out)
    img.save(os.path.join(checkpoint_dir, filename))


def save_final_image(image_arr, filename):
    image = Image.fromarray(image_arr)
    image.save(os.path.join(output_dir, filename))

if __name__ == '__main__':
    main()
