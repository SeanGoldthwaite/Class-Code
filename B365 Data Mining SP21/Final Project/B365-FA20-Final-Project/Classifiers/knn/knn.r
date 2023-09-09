distance <- function(x, y, z, i, j, k) {
  return(sqrt((x-i)^2 + (y-j)^2 + (z-k)^2))
}

data = read.csv("../../data.csv" ,stringsAsFactors=FALSE, sep=",")

total_length = nrow(data)
labels = colnames(table(data$avgLen, data$label))
num_labels = length(labels)

#remove mcc column because it is categorical
X = data[c(1, 2, 4, 5)]

#normalize data
for (attr in 1:(ncol(X) - 1)) {
  max = X[which.max(X[,attr]),attr]
  min = X[which.min(X[,attr]),attr]
  X[,attr] = (X[,attr] - min) / (max - min)
}

reps = 15
correct_knn = 0
correct_weighted = 0
total = 0

knn_pred = c()
weighted_pred = c()
true_labels = c()

for (rep in 1:reps) {
  cat("Calculating repetition", rep, "out of", reps,"\n")
  
  #shuffle data
  X = X[sample(total_length),]
  
  #5-fold cross validation
  split = total_length * (4/5)
  X_train = X[1:split,]
  X_test = X[split:nrow(X),]
  
  train_length = nrow(X_train)
  test_length = nrow(X_test)
  
  #knn
  distances = matrix(nrow = test_length, ncol = train_length)
  nearest_neighbors = matrix(nrow = test_length, ncol = train_length)
  
  #calculate distances from every test point to every train point
  for (row in 1:test_length) {
    for (col in 1:train_length) {
      distances[row,col] = distance(X_test[row,1], X_test[row,2], X_test[row,3], X_train[col,1], X_train[col,2], X_train[col,3])
    }
    nearest_neighbors[row,] = X[order(distances[row,]), 4]
  }
  test_labels = X_test[,4]
  
  knn_predictions = rep(0, test_length)
  confidence_predictions = rep(0, test_length)
  
  confidence_matrix = matrix(rep(1:(train_length - 1)), nrow = train_length - 1, ncol = 1)
  
  #applies the function 1/e^k on confidence_matrix where k is the index
  confidence_matrix = 1 / exp(confidence_matrix)
  confidence = matrix(nrow = test_length, ncol = num_labels)
  colnames(confidence) = labels
  
  for (row in 1:test_length) {
    #knn predictions
    tab = table(nearest_neighbors[row, 2:6])
    knn_predictions[row] = names(tab)[which.max(tab)]
    
    #confidence predictions
    for (lab in 1:num_labels) {
      confidence[row,lab] =  (nearest_neighbors[row,2:train_length] == labels[lab]) %*% confidence_matrix
    }
    confidence_predictions[row] = names(which.max(confidence[row,]))[1]
  }
  correct_knn = correct_knn + sum(knn_predictions == test_labels)
  correct_weighted = correct_weighted + sum(confidence_predictions == test_labels)
  total = total + length(test_labels)
  
  knn_pred = cbind(knn_pred, knn_predictions)
  weighted_pred = cbind(weighted_pred, confidence_predictions)
  true_labels = cbind(true_labels, test_labels)
}
cat("Regular knn accuracy =", (correct_knn / total),"\n")
cat("Weighted knn accuracy =", (correct_weighted / total),"\n")

print(table(knn_pred, true_labels))
print(table(weighted_pred, true_labels))