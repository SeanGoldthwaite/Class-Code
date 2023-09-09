library(e1071)
library(caTools) 
library(caret)
library(tidyr)
library(dplyr)

PATH = '../../data.csv'

clean_data <- function(csv_path) {
  data = read.csv(csv_path)
  data = as.data.frame(data)
  
  data[,1] = as.factor(round(data[,1],1))
  data[,2] = as.factor(round(data[,2],1))
  data[,3] = factor(data[,3])
  data[,4] = as.factor(round(data[,4],2))
  data[,5] = factor(data[,5])
  
  return(data)
}

train_classifier <- function(dataset) {
  cl = naiveBayes(dataset[,1:ncol(dataset)-1],dataset[,ncol(dataset)])
  return(cl)
}

test <- function(classifier,test_set) {
  y_pred = predict(classifier, test_set)
  
  cm = table(test_set$label, y_pred, dnn = c('Labels','Predictions'))
  
  return(cm)
}

accuracy <- function(dataset) {
  len = nrow(dataset)
  true_cond = 0
  total = 0
  for (i in 1:len) {
    true_cond = true_cond + dataset[i,i]
    for (j in 1:len) {
      total = total + dataset[i,j]
    }
  }
  return (true_cond / total)
}

avg_accuracy <- function(dataset,n) {
  sum = 0
  for (i in 1:n) {
    sum = sum + accuracy(try_attributes(dataset))
  }
  return (sum/n)
}

try_attributes <- function(dataset,split=T,col=NULL) {
  
  if (!is.null(col)) {
    
    X = dataset[,-col]
    
  } else {
    
    X = dataset
    
  }
  
  split = sample.split(data, SplitRatio = 0.7)
  train_cl = subset(X, split == "TRUE")
  test_cl = subset(X, split == "FALSE")
  
  classifier_cl = train_classifier(train_cl)
  
  y_pred = predict(classifier_cl, test_cl)
  
  cm = table(test_cl$label, y_pred, dnn = c('Labels','Predictions'))
  # print(colnames(X))
  
  return(cm)
}

my_bayes <- function(dataset) {
  X = dataset[,1:ncol(dataset)-1]
  y = dataset[,ncol(dataset)]
  
  X[,"apoFreq"] = if(!is.factor("apoFreq")) 0 else X[,"apoFreq"]
  
  labels = unique(y)
  
  for (i in 1:ncol(dataset)) {
    
  }
  
  t = table(training_data)
  
  c_head = array(0,c(dim(t)[1],dim(t)[2],dim(t)[3],dim(t)[4]))
  for (v in 1:dim(t)[1]) {
    for (w in 1:dim(t)[2]) {
      for (i in 1:dim(t)[3]) {
        for (j in 1:dim(t)[4]) {
          m = which.max(t[v,w,i,j,])
          c_head[v,w,i,j] = m
        }
      }
    }
  }
  
  dimnames(c_head) = dimnames(t)[1:4]
  
  error = 0
  for (i in 1:nrow(X)) {
    print(c_head[as.character(X[i,1]),as.character(X[i,2]),as.character(X[i,3]),as.character(X[i,4])])
    if (c_head[as.character(X[i,1]),as.character(X[i,2]),as.character(X[i,3]),as.character(X[i,4])] != y[i]) { error = error + 1 }
  }
  error = error/nrow(X)
  
  return(error)
  
}