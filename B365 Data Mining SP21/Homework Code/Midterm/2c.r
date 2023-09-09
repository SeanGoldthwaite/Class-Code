data = UCBAdmissions

data = data["Admitted",,]
table = apply(data, c("Gender"), sum)
print(table)