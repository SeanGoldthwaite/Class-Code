setwd("D:\\Classes\\B365\\Data")
data = read.csv2("./vocab.csv",stringsAsFactors=FALSE,sep=",")


len = length(data[,1])

education = data[,4] #list of in-order education values
vocab = data[,5] #list of in-order vocabulary values

#1a answer
X = cbind(rep(1, len), as.matrix(education))
Y = as.matrix(vocab)

solution = solve(t(X) %*% X, t(X) %*% Y)

a = solution[1,1] #slope
b = solution[2,1] #y-intercept

#1b answer
sprintf("a = %g, b = %g", a, b)
sprintf("Equation: y_i = (%g * x_i) + %g", a, b)

sunflowerplot(education, vocab)