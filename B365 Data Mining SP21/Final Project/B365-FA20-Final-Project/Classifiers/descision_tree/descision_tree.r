library(rpart)

setwd("D:/Classes/B365/Data")
data = read.csv("data.csv",stringsAsFactors=FALSE, sep=",")

labels = colnames(table(data$avgLen, data$label))

#shuffle dataset
data = data[sample(nrow(data)),]

reps = 100
correct = 0
total = 0

for (rep in 1:reps) {
  cat("Calculating repetition", rep, "out of", reps,"\n")
  
  #5-fold cross validation
  split = nrow(data) * (4/5)
  training_set = data[1:split,]
  testing_set = data[split:nrow(data),]
  
  test_labels = testing_set[,5]
  
  fit = rpart(label ~ 
                avgLen + 
                stDev + 
                apoFreq,
              data = training_set,
              method="class")
  
  #prunes tree with cp that gives min xerror
  prune(fit, fit$cptable[which.min(fit$cptable[,"xerror"]),"CP"])
  
  predictions = predict(fit, newdata=testing_set, type="class")
  
  correct = correct + sum(predictions == test_labels)
  total = total + length(test_labels)
}
cat("Decision tree accuracy =", (correct / total),"\n")