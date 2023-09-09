# -*- coding: utf-8 -*-
"""
Created on Sun Apr 24 17:41:39 2022

@author: aidan
"""
from itertools import permutations
from sklearn.cluster import KMeans
import sys
from skimage.io import imread
from skimage.color import rgb2gray
from numpy import linalg as LA
import matplotlib.pyplot as plt
import cv2
import numpy as np
from scipy.ndimage.filters import convolve


def gaussian_filter(sigma): 
    #failsafe implemented here
    #sometimes sigma is negative (derived from keypoints). This is in place to ensure no errors are thrown
    if (sigma < 0):
        print("sig")
        sigma = -1 * sigma
    size = 2*np.ceil(3*sigma)+1 
    x, y = np.mgrid[-size//2 + 1:size//2 + 1, -size//2 + 1:size//2 + 1] 
    g = np.exp(-((x**2 + y**2)/(2.0*sigma**2))) / (2*np.pi*sigma**2)
    return g/g.sum()
    
def generate_octave(init_level, s, sigma): 
  octave = [init_level] 
  k = 2**(1/s) 
  kernel = gaussian_filter(k * sigma) 
  for p in range(s+2): 
    next_level = convolve(octave[-1], kernel) 
    octave.append(next_level) 
  return octave

def generate_gaussian_pyramid(im, num_octave, s, sigma): 
  pyr = [] 
  for _ in range(num_octave): 
    octave = generate_octave(im, s, sigma) 
    pyr.append(octave) 
    im = octave[-3][::2, ::2] 
  return pyr

def generate_DoG_octave(gaussian_octave): 
  octave = [] 
  for i in range(1, len(gaussian_octave)):   
    octave.append(gaussian_octave[i] - gaussian_octave[i-1]) 
  return np.concatenate([o[:,:,np.newaxis] for o in octave], axis=2) 

def generate_DoG_pyramid(gaussian_pyramid): 
  pyr = [] 
  for gaussian_octave in gaussian_pyramid: 
    pyr.append(generate_DoG_octave(gaussian_octave)) 
  return pyr

def get_candidate_keypoints(D, w=16): 
  candidates = [] 
  D[:,:,0] = 0 
  D[:,:,-1] = 0 
  for i in range(w//2+1, D.shape[0] - w//2-1): 
    for j in range(w//2+1, D.shape[1]-w//2-1): 
      for k in range(1, D.shape[2]-1): 
        patch = D[i-1:i+2, j-1:j+2, k-1:k+2] 
        if np.argmax(patch) == 13 or np.argmin(patch) == 13: 
          candidates.append([i, j, k]) 
  return candidates

def localize_keypoint(D, x, y, s): 
  dx = (D[y,x+1,s]-D[y,x-1,s])/2.
  dy = (D[y+1,x,s]-D[y-1,x,s])/2.
  ds = (D[y,x,s+1]-D[y,x,s-1])/2.
  dxx = D[y,x+1,s]-2*D[y,x,s]+D[y,x-1,s]
  dxy = ((D[y+1,x+1,s]-D[y+1,x-1,s]) - (D[y-1,x+1,s]-D[y-1,x-1,s]))/4.
  dxs = ((D[y,x+1,s+1]-D[y,x-1,s+1]) - (D[y,x+1,s-1]-D[y,x-1,s-1]))/4.
  dyy = D[y+1,x,s]-2*D[y,x,s]+D[y-1,x,s]
  dys = ((D[y+1,x,s+1]-D[y-1,x,s+1]) - (D[y+1,x,s-1]-D[y-1,x,s-1]))/4.
  dss = D[y,x,s+1]-2*D[y,x,s]+D[y,x,s-1] 
  J = np.array([dx, dy, ds]) 
  HD = np.array([ [dxx, dxy, dxs], [dxy, dyy, dys], [dxs, dys, dss]]) 
  offset = -LA.inv(HD).dot(J)
  return offset, J, HD[:2,:2], x, y, s

def find_keypoints_for_DoG_octave(D, R_th, t_c, w): 
  candidates = get_candidate_keypoints(D, w)
  keypoints = [] 
  for i, cand in enumerate(candidates): 
    y, x, s = cand[0], cand[1], cand[2] 
    offset, J, H, x, y, s = localize_keypoint(D, x, y, s) 
    contrast = D[y,x,s] + .5*J.dot(offset) 
    if abs(contrast) < t_c: continue 
    w, v = LA.eig(H) 
    r = w[1]/w[0] 
    R = (r+1)**2 / r 
    if R > R_th: continue 
    kp = np.array([x, y, s]) + offset
    if kp[1] >= D.shape[0] or kp[0] >= D.shape[1]: continue
    keypoints.append(kp)
  return np.array(keypoints)

def get_keypoints(DoG_pyr, R_th, t_c, w): 
  kps = [] 
  for D in DoG_pyr: 
    kps.append(find_keypoints_for_DoG_octave(D, R_th, t_c, w)) 
  return kps

def assign_orientation(kps, octave, num_bins=36): 
  new_kps = [] 
  bin_width = 360//num_bins 
  
  for kp in kps: 
    cx, cy, s = int(kp[0]), int(kp[1]), int(kp[2]) 
    s = np.clip(s, 0, octave.shape[2]-1) 
    sigma = kp[2]*1.5
    w = int(2*np.ceil(sigma)+1) 
    kernel = gaussian_filter(sigma) 
    L = octave[...,s]
    hist = np.zeros(num_bins, dtype=np.float32) 
    for oy in range(-w, w+1): 
      for ox in range(-w, w+1): 
        x, y = cx+ox, cy+oy 
        if x < 0 or x > octave.shape[1]-1: continue 
        elif y < 0 or y > octave.shape[0]-1: continue 
    
        #FAILSAFE IMPLEMENTED HERE
        #some indices return negative. This is in place to ensure that does not happen, although the root cause is still unknown
        sumx = ox+w
        sumy = oy+w
        if(ox+w >= len(kernel[0])):
            print("x")
            sumx = len(kernel[0])-1
        if (oy+w >= len(kernel)):
            print("y")
            sumy = len(kernel)-1
        m, theta = get_grad(L, x, y)
        weight = kernel[sumy, sumx] * m
        #End failsafe
        bin = quantize_orientation(theta, num_bins) 
        hist[bin] += weight 
    max_bin = np.argmax(hist) 
    new_kps.append([kp[0], kp[1], kp[2], fit_parabola(hist, max_bin, bin_width)]) 
    max_val = np.max(hist) 
    for binno, val in enumerate(hist): 
      if binno == max_bin: continue 
      if .8 * max_val <= val: 
        new_kps.append([kp[0], kp[1], kp[2], fit_parabola(hist, binno, bin_width)])
  return np.array(new_kps)

def fit_parabola(hist, binno, bin_width): 
  centerval = binno*bin_width + bin_width/2. 
  if binno == len(hist)-1: rightval = 360 + bin_width/2. 
  else: rightval = (binno+1)*bin_width + bin_width/2. 
  if binno == 0: leftval = -bin_width/2. 
  else: leftval = (binno-1)*bin_width + bin_width/2. 
  A = np.array([ 
    [centerval**2, centerval, 1], 
    [rightval**2, rightval, 1], 
    [leftval**2, leftval, 1]]) 
  b = np.array([ 
    hist[binno], 
    hist[(binno+1)%len(hist)], 
    hist[(binno-1)%len(hist)]]) 
  x = LA.lstsq(A, b, rcond=None)[0] 
  if x[0] == 0: x[0] = 1e-6 
  return -x[1]/(2*x[0])

def cart_to_polar_grad(dx, dy): 
  m = np.sqrt(dx**2 + dy**2) 
  theta = (np.arctan2(dy, dx)+np.pi) * 180/np.pi 
  return m, theta

def get_grad(L, x, y): 
  dy = L[min(L.shape[0]-1, y+1),x] - L[max(0, y-1),x]
  dx = L[y,min(L.shape[1]-1, x+1)] - L[y,max(0, x-1)]
  return cart_to_polar_grad(dx, dy)

def quantize_orientation(theta, num_bins): 
  bin_width = 360//num_bins 
  return int(np.floor(theta)//bin_width)

def get_local_descriptors(kps, octave, w=16, num_subregion=4, num_bin=8): 
  descs = [] 
  bin_width = 360//num_bin
  for kp in kps: 
    cx, cy, s = int(kp[0]), int(kp[1]), int(kp[2]) 
    s = np.clip(s, 0, octave.shape[2]-1)
    kernel = gaussian_filter(w/6) # gaussian_filter multiplies sigma by 3 
    L = octave[...,s]
    t, l = max(0, cy-w//2), max(0, cx-w//2) 
    b, r = min(L.shape[0], cy+w//2+1), min(L.shape[1], cx+w//2+1) 
    #NOTE: issue with cx value. when cx is negative, the function breaks. 
    #cx is derived above from keypoints
    patch = L[t:b, l:r] 
    dx, dy = get_patch_grads(patch) 
    if dx.shape[0] < w+1: 
     if t == 0:
       kernel = kernel[:, kernel.shape[0]-kernel.shape[0]-dx.shape[0]:]
     else:
       kernel = kernel[:dx.shape[0]]
    if dx.shape[1] < w+1:
     if l == 0:
       kernel = kernel[:, kernel.shape[1]-kernel.shape[1]-dx.shape[1]:]
     else:
       kernel = kernel[:dx.shape[1]]
    if dy.shape[0] < w+1:
     if t == 0:
       kernel = kernel[:,  kernel.shape[0]-kernel.shape[0]-dy.shape[0]:]
     else:
       kernel = kernel[:dy.shape[0]]
    if dy.shape[1] < w+1:
     if l == 0:
       kernel = kernel[:, kernel.shape[1]-kernel.shape[1]-dy.shape[1]:]
     else:
       kernel = kernel[:dy.shape[1]]
    """
    if dx.shape[0] < w+1: 
      if t == 0: kernel = kernel[kernel.shape[0]-dx.shape[0]:] 
      else: kernel = kernel[:dx.shape[0]] 
    if dx.shape[1] < w+1: 
      if l == 0: kernel = kernel[kernel.shape[1]-dx.shape[1]:] 
      else: kernel = kernel[:dx.shape[1]] 
    if dy.shape[0] < w+1: 
      if t == 0: kernel = kernel[kernel.shape[0]-dy.shape[0]:] 
      else: kernel = kernel[:dy.shape[0]] 
    if dy.shape[1] < w+1: 
      if l == 0: kernel = kernel[kernel.shape[1]-dy.shape[1]:] 
      else: kernel = kernel[:dy.shape[1]] 
      """
    m, theta = cart_to_polar_grad(dx, dy) 
    dx, dy = dx*kernel, dy*kernel 
    subregion_w = w//num_subregion 
    featvec = np.zeros(num_bin * num_subregion**2, dtype=np.float32) 
    for i in range(0, subregion_w): 
      for j in range(0, subregion_w): 
        t, l = i*subregion_w, j*subregion_w 
        b, r = min(L.shape[0], (i+1)*subregion_w), min(L.shape[1], (j+1)*subregion_w) 
        hist = get_histogram_for_subregion(m[t:b, l:r].ravel(), theta[t:b, l:r].ravel(), num_bin, kp[3], bin_width, subregion_w) 
        featvec[i*subregion_w*num_bin + j*num_bin:i*subregion_w*num_bin + (j+1)*num_bin] = hist.flatten() 
    featvec /= max(1e-6, LA.norm(featvec))        
    featvec[featvec>0.2] = 0.2        
    featvec /= max(1e-6, LA.norm(featvec))    
    descs.append(featvec)
  return np.array(descs)

def get_patch_grads(p): 
  r1 = np.zeros_like(p) 
  r1[-1] = p[-1] 
  r1[:-1] = p[1:] 
  r2 = np.zeros_like(p) 
  r2[0] = p[0] 
  r2[1:] = p[:-1] 
  dy = r1-r2 
  r1[:,-1] = p[:,-1] 
  r1[:,:-1] = p[:,1:] 
  r2[:,0] = p[:,0] 
  r2[:,1:] = p[:,:-1] 
  dx = r1-r2 
  return dx, dy

def get_histogram_for_subregion(m, theta, num_bin, reference_angle, bin_width, subregion_w): 
  hist = np.zeros(num_bin, dtype=np.float32) 
  c = subregion_w/2 - .5
  #modified to match the format in the github, different from skeleton file on canvas
  for i, (mag, angle) in enumerate(zip(m, theta)):
    angle = (angle-reference_angle) % 360        
    binno = quantize_orientation(angle, num_bin)        
    vote = mag      
   
    hist_interp_weight = 1 - abs(angle - (binno*bin_width + bin_width/2))/(bin_width/2)        
    vote *= max(hist_interp_weight, 1e-6)         
    gy, gx = np.unravel_index(i, (subregion_w, subregion_w))        
    x_interp_weight = max(1 - abs(gx - c)/c, 1e-6)            
    y_interp_weight = max(1 - abs(gy - c)/c, 1e-6)        
    vote *= x_interp_weight * y_interp_weight         
    hist[binno] += vote
  hist /= max(1e-6, LA.norm(hist)) 
  hist[hist>0.2] = 0.2 
  hist /= max(1e-6, LA.norm(hist))
  return hist

class SIFT(object): 
    #ORIGINALLY OCTAVE = 4
  def __init__(self, im, s=3, num_octave=2, s0=1.3, sigma=1.6, r_th=10, t_c=0.03, w=16): 
    self.im = convolve(rgb2gray(im), gaussian_filter(s0)) 
    self.s = s 
    self.sigma = sigma 
    self.num_octave = num_octave
    self.t_c = t_c 
    self.R_th = (r_th+1)**2 / r_th 
    self.w = w 
  def get_features(self): 
    gaussian_pyr = generate_gaussian_pyramid(self.im, self.num_octave, self.s, self.sigma) 
    DoG_pyr = generate_DoG_pyramid(gaussian_pyr) 
    kp_pyr = get_keypoints(DoG_pyr, self.R_th, self.t_c, self.w) 
    feats = [] 
    for i, DoG_octave in enumerate(DoG_pyr): 
        kp_pyr[i] = assign_orientation(kp_pyr[i], DoG_octave) 
        feats.append(get_local_descriptors(kp_pyr[i], DoG_octave)) 
    self.kp_pyr = kp_pyr 
    self.feats = feats 
    return feats

"""
im1 = imread("part1-images/bigben_7.jpg")
im2 = imread("part1-images/sanmarco_1.jpg")


sift_test1 = SIFT(im1)
feats1 = sift_test1.get_features()
keypyr1 = sift_test1.kp_pyr

feats1, ax = plt.subplots(1, sift_test1.num_octave)
for i in range(sift_test1.num_octave):
    ax[i].imshow(sift_test1.im)
    scaled_kps = keypyr1[i] * (2**i)
    ax[i].scatter(scaled_kps[:,0], scaled_kps[:,1], c='r', s=2.5)
plt.show()


sift_test2 = SIFT(im2)
feats2 = sift_test2.get_features()
keypyr2 = sift_test2.kp_pyr

feats2, ax = plt.subplots(1, sift_test2.num_octave)
for i in range(sift_test2.num_octave):
    ax[i].imshow(sift_test2.im)
    scaled_kps = keypyr2[i] * (2**i)
    ax[i].scatter(scaled_kps[:,0], scaled_kps[:,1], c='r', s=2.5)
plt.show()
"""
#this function is not used when comparing images for the purpose of clustering as sift is built into this image and running sift N(n-1) * 2 times would take
#a tremendous amount of time. However, this function does work as intended returning the number of matches
def compareimages (im1, im2):
    threshold = 0.87
    matches = 0
    sift1 = SIFT(im1)
    sift2 = SIFT(im2)
    feats1 = sift1.get_features()
    feats2 = sift2.get_features()
    dist = np.zeros((len(feats1[0]), len(feats2[0])))
    closest = np.zeros((len(feats1[0])))
    # i is features in first image
    for i in range(len(feats1[0])):
        firstdist = 9999
        seconddist = 9999
        #j is features in second image
        for j in range(len(feats2[0])):
            dist[i][j] = cv2.norm(feats1[0][i], feats2[0][j], cv2.NORM_L2)
            if dist[i][j] < firstdist:
                firstdist = dist[i][j]
            elif dist[i][j] < seconddist:
                seconddist = dist[i][j]
        closest[i] = firstdist / seconddist
            #dist[i][j] is the distance between feature i of image 1 and feature j of image 2
    #find best match, second best match
    print(closest)
    #sum the distances together and divide by the number of distances
    sum = np.sum(closest)/len(closest)
    print("distance between im1 and im2: ", sum)
    for i in range(len(closest)):
        if (closest[i] < threshold):
            matches += 1
    print("threshold: ", threshold, "number of matches (distance less than threshold): ", matches)
    print("ratio of matches to total points: ", matches/len(closest))
    return matches


big = []
col = []
eif = []
emp = []
lon = []
lou = []
notr = []
san = []
tat = []
tra = []

def sortimage(name):
    if ("bigben" in name):
        big.append(name)
    elif ("colo" in name):
        col.append(name)
    elif ("eiffel" in name):
        eif.append(name)
    elif ("empire" in name):
        emp.append(name)
    elif ("london" in name):
        lon.append(name)
    elif ("louvre" in name):
        lou.append(name)
    elif ("notre" in name):
        notr.append(name)
    elif ("sanm" in name):
        san.append(name)
    elif ("tate" in name):
        tat.append(name)
    elif ("traf" in name):
        tra.append(name)
    else:
        print("MISTAKE MADE WHEN SORTING IMAGES INTO TRUE CLASSES")

def checkgroup(first, second, labels, images):
    #index in images, compare to labels
    firstgroup = labels[images.index(first)]
    secondgroup = labels[images.index(second)]
    return firstgroup == secondgroup

def checkname(first, second):
    if ("bigben" in first):
        return "bigben" in second
    elif ("colo" in first):
        return "colo" in second
    elif ("eiffel" in first):
        return "eiffel" in second
    elif ("empire" in first):
        return "empire" in second
    elif ("london" in first):
        return "london" in second
    elif ("louvre" in first):
        return "louvre" in second
    elif ("notre" in first):
        return "notre" in second
    elif ("sanm" in first):
        return "sanm" in second
    elif ("tate" in first):
        return "tate" in second
    elif ("traf" in first):
        return "traf" in second
    else:
        print( "ERROR IN CHECK")  
        


"""
images = ["part1-images/bigben_7.jpg", "part1-images/sanmarco_1.jpg", "part1-images/colosseum_5.jpg", "part1-images/bigben_6.jpg"]
groups = 3
output = "output.txt"
"""

def bestmatch(im1, im2, n=1):
    pairs = []
    imagepairs = []
    firstindex = 0
    secondindex = 0
    sift1 = SIFT(im1)
    sift2 = SIFT(im2)
    feats1 = sift1.get_features()
    feats2 = sift2.get_features()
    #desired index for point x,y in im1 and point x_,y_ in im2 is
    #for feature i in im1 and feature j in im2, (x,y) = (sift1.kp_pyr[0][pairs[0][0],0], sift1.kp_pyr[0][pairs[0][1],1]
    #replacing first index of pairs for the corresponding N
    dist = np.zeros((len(feats1[0]), len(feats2[0])))
    closest = np.zeros((len(feats1[0])), dtype=tuple)
    # i is features in first image
    for i in range(len(feats1[0])):
        firstdist = 9999
        seconddist = 9999
        #j is features in second image
        for j in range(len(feats2[0])):
            dist[i][j] = cv2.norm(feats1[0][i], feats2[0][j], cv2.NORM_L2)
            if dist[i][j] < firstdist:
                firstdist = dist[i][j]
                firstindex = i
            elif dist[i][j] < seconddist:
                seconddist = dist[i][j]
                secondindex = j
        closest[i] = (firstindex, secondindex)
    while (len(imagepairs) < n):
        i = len(imagepairs)
        ind = np.unravel_index(np.argmin(dist, axis=None), np.shape(dist))
        pairs.append(ind)
        dist[ind] = 99
            #dist[i][j] is the distance between feature i of image 1 and feature j of image 2
        pair1 = (sift1.kp_pyr[0][pairs[i][0],0], sift1.kp_pyr[0][pairs[i][0],1])
        pair2 = (sift2.kp_pyr[0][pairs[i][1],0], sift2.kp_pyr[0][pairs[i][1],1])
        pair = (pair1, pair2)
        if (pair not in imagepairs):
            imagepairs.append(pair)
        else:
            pairs.remove(pairs[-1])
    print(imagepairs)
    return imagepairs

#im1 = imread("part2-images/book1.jpg")
#im2 = imread("part2-images/book2.jpg")
#bestmatch(im1, im2, n=7)


def run():
    if (sys.argv[1] == "part1"):
        print ("running from a2.py")
        return
    groups = (int)(sys.argv[1])
    images = sys.argv[2: -1]
    output = sys.argv[-1]
    features = []
    # each image is made of some unknown number of features
    #and each feature is a 128 dimensional vector
    #a naive approach may be to average all of the values in the feature space, leaving only one 128 dim vector
    for i in images:
        sortimage(i)
        im = imread(i)
        sift = SIFT(im)
        feats = sift.get_features()
        sum = np.sum(feats[0], axis=0)
        features.append(sum)
    #kmeans
    km = KMeans(n_clusters = groups)
    km = km.fit(features)
    labels = km.labels_
    f = open(output, "w")
    f.write("")
    f.close()
    file = open(output, "a")
    for i in range(groups):
        for j in range(len(images)):
            if (labels[j] == i):
                file.write(images[j] + " ")
            
        file.write("\n \n")
    file.close()
    print(labels)
    pairs = len(images) * (len(images)-1)
    truepos = 0
    trueneg = 0
    perm = permutations(images, 2)
    for i in list(perm):
        if (checkname(i[0], i[1]) == False):
            if (checkgroup(i[0], i[1], labels, images) == False):
                trueneg += 1
        elif (checkname(i[0], i[1]) == True):
            if (checkgroup(i[0], i[1], labels, images) == True):
                truepos += 1
        else:
            print("ERROR IN PAIRS")
    print("ACCURACY: ", ((truepos+trueneg)/pairs))

run()

def runargs(groups, images, output):
    features = []
    # each image is made of some unknown number of features
    #and each feature is a 128 dimensional vector
    #a naive approach may be to average all of the values in the feature space, leaving only one 128 dim vector
    for i in images:
        sortimage(i)
        im = imread(i)
        sift = SIFT(im)
        feats = sift.get_features()
        sum = np.sum(feats[0], axis=0)
        features.append(sum)
    #kmeans
    km = KMeans(n_clusters = groups)
    km = km.fit(features)
    labels = km.labels_
    f = open(output, "w")
    f.write("")
    f.close()
    file = open(output, "a")
    for i in range(groups):
        for j in range(len(images)):
            if (labels[j] == i):
                file.write(images[j] + " ")
            
        file.write("\n \n")
    file.close()
    print(labels)
    pairs = len(images) * (len(images)-1)
    truepos = 0
    trueneg = 0
    perm = permutations(images, 2)
    for i in list(perm):
        if (checkname(i[0], i[1]) == False):
            if (checkgroup(i[0], i[1], labels, images) == False):
                trueneg += 1
        elif (checkname(i[0], i[1]) == True):
            if (checkgroup(i[0], i[1], labels, images) == True):
                truepos += 1
        else:
            print("ERROR IN PAIRS")
    print("ACCURACY: ", ((truepos+trueneg)/pairs))
