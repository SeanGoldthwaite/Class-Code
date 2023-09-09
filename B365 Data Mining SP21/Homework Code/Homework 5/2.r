setwd("D:\\Classes\\B365\\Data")
data = read.csv("ais.csv",stringsAsFactors=FALSE, sep=",")

len = nrow(data)

X = as.matrix(cbind(rep(1, len), data[,3:12]))
Y = as.matrix(data[,2])

dimnames(X)[[2]][1] = "constant"

solution = solve(t(X) %*% X, t(X) %*% Y)

#2a Answer
print(solution)

predictions = X %*% solution
error = Y - predictions
sse = t(error) %*% error

#2b answer
print(sse)

columns = ncol(X) #number of columns in data

sse_collection = matrix(nrow = 1, ncol = columns - 1, dimnames = list(c("sse"), c("wcc","hc","hg","ferr","bmi","ssi","pcBfat","lbm","ht","wt"))) #sse obtained by omitting each corresponding variable

#2c answer
for (var in 2:columns) {#omit 1 because it is the "constant" column and not a variable
  x_rev = X[,-var]
  solution = solve(t(x_rev) %*% x_rev, t(x_rev) %*% Y)
  predictions = x_rev %*% solution
  error = Y - predictions
  sse_collection[,var - 1] = t(error) %*% error
}

print(sse_collection)