setwd("D:/Classes/B365/Data")
data = read.csv("data.csv",stringsAsFactors=FALSE, sep=",")

labels = colnames(table(data$avgLen, data$label))
num_labels = length(labels)

values = c(1, 2, 4)
value_names = colnames(data)[values]

averages = matrix(nrow = 4, ncol = num_labels)

for (label in 1:num_labels) {
  label_data = data[data[,5] == labels[label],]
  for (val in values) {
    averages[val, label] = sum(label_data[,val]) / nrow(label_data)
  }
}
averages = averages[-3,]
colnames(averages) = labels
rownames(averages) = value_names
print(averages)

for (row in 1:nrow(averages)) {
  barplot(averages[row,], main=value_names[row])
}


for (label in 1:num_labels) {
  label_data = data[data[,5] == labels[label],]
  print(table(label_data[,3], label_data[,5]))
}