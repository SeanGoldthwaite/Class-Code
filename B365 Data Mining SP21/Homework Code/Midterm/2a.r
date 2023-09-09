data = UCBAdmissions

table = apply(UCBAdmissions, c("Dept", "Admit"), sum)
print(table)
mosaicplot(table)