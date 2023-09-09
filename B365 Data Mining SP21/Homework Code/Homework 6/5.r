data = freeny

Y = as.matrix(data[,1])
X_vars = as.matrix(data[,2:ncol(data)])

n = nrow(X_vars)
d = ncol(X_vars)

X = cbind(X_vars, rep(1, n))

solution = solve(t(X) %*% X, t(X) %*% Y)
errors = Y - (X %*% solution)
sse = t(errors) %*% errors

removal_order = rep(NA, d)
removal_sse = rep(NA, d)
X_temp = X

for (removal in 1:d) {
  num_variables = ncol(X_temp) - 1
  sse_collection = rep(NA, num_variables)
  
  for (var in 1:num_variables) {
    #excludes var from X_temp
    X_removed = X_temp[,-var]
    
    #calculates regression and sse
    solution = solve(t(X_removed) %*% X_removed, t(X_removed) %*% Y)
    errors = Y - (X_removed %*% solution)
    sse = t(errors) %*% errors
    
    sse_collection[var] = sse
  }
  min_sse = which.min(sse_collection)
  removal_order[removal] = colnames(X_temp)[min_sse]
  removal_sse[removal] = sse_collection[min_sse]
  
  #removes the variable that produced the min sse when excluded
  X_temp = X_temp[,-min_sse]
}
#beautifying results
results = rbind(removal_order, removal_sse)
colnames(results) = c("1st", "2nd", "3rd", "4th")
rownames(results) = c("var name", "sse after removal")

print(results)