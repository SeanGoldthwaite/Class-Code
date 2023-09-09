# implement a nearest neighbor classifier on the Fisher iris data.  

data(iris)   

d = as.matrix(dist(iris[,1:4]))  # the Euclidean distance matrix d[i,j] is dist from ith flower to jth flower 150x150 matrix
n = nrow(iris);

class = as.numeric(iris[,5])  # the true classes

classhat = rep(0,n);	      # our estimated classes
for (i in 1:n) {
  nn = order(d[i,])[2:6] #5 nearest neighbors
  for (j in 1:length(nn)) { #converts iris numbers to their classification
    nn[j] = class[nn[j]]
  }
  j = names(sort(summary(as.factor(nn)), decreasing=T)[1:1]) #finds most common class label in nearest neighbors
  
  classhat[i] = strtoi(j)
}
print(class)
print(classhat)

errors = sum(abs(classhat - class))
sprintf("%g total errors or %g%%", errors, errors/n)