data = matrix(c(0,3,1,2,2,3,3,6), nrow = 4, ncol = 2, byrow = TRUE)

rownames(data) = c("p1", "p2", "p3", "p4")
#colnames(data) = c("X", "Y")

print(data)

length = length(data[,1])

x_coords = as.matrix(data[,1])
y_coords = as.matrix(data[,2])
Y = as.matrix(data[,2])

max_degree = 3

sse_collection = matrix(nrow = 1, ncol = max_degree)

for (degree in 1:max_degree) {
  X = rep(1, length)
  Y = y_coords
  
  for (i in 1:degree) {
    X = cbind(X, x_coords ^ i)
  }
  
  solution = solve(t(X) %*% X, t(X) %*% Y)
  
  error = Y - (X %*% solution)
  sse = t(error) %*% error
  sse_collection[degree] = sse
  
  cat("\n\nDegree: ")
  cat(degree)
  cat("\nCoefficients (x^0 -> x^degree): ")
  cat(solution)
  cat("\nsse: ")
  cat(sse)
}

rownames(sse_collection) = c("sse")
colnames(sse_collection) = c("x^1","x^2","x^3")

print(sse_collection)

