data = UCBAdmissions

data = data[,,"F"]
table = apply(data, c("Gender", "Admit"), sum)
print(table)
mosaicplot(table)