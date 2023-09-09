data = read.csv("data.csv",stringsAsFactors=FALSE, sep=",")

labels = colnames(table(data$avgLen, data$label))
num_labels = length(labels)

values = c(1, 2, 4)
value_names = colnames(data)[values]

averages = matrix(nrow = 4, ncol = num_labels)

#calculates averages of each value per label
for (label in 1:num_labels) {
  label_data = data[data[,5] == labels[label],]
  for (val in values) {
    averages[val, label] = sum(label_data[,val]) / nrow(label_data)
  }
}
#beautifying the averages
averages = averages[-3,]
colnames(averages) = labels
rownames(averages) = value_names
print(averages)

#prints bar-plots for each value
for (row in 1:nrow(averages)) {
  barplot(averages[row,], main=value_names[row])
}

#tables showing how many times a character was the most common character in texts of a given language
for (label in 1:num_labels) {
  label_data = data[data[,5] == labels[label],]
  print(table(label_data[,3], label_data[,5]))
}