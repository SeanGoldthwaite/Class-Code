distance = function(a, b) {
  return(sqrt((a[[1]] - b[1])^2 + (a[[2]] - b[2])^2))
}

points = matrix(list(0,1,1,1,0,2,1,2,0,3,1,3), nrow = 6, ncol = 2, byrow = TRUE)
X = points[,1]
Y = points[,2]
#print(points)
#plot(X, Y)

n = nrow(points)
k = 2

cluster_centers = matrix(runif(2 * k), nrow = k, ncol = 2, dimnames=list(c("m1", "m2"), c("X", "Y")))

cluster_centers[1,1] = .5
cluster_centers[1,2] = 1
cluster_centers[2,1] = .5
cluster_centers[2,2] = 2.5

print(cluster_centers)

for (i in 1:1) {
  distances = matrix(nrow = n, ncol = k)
  classifications = rep(0, n)
  means = rep(0, k)
  for (point in 1:n) {
    for (cluster in 1:k) {
      distances[point,cluster] = distance(points[point,], cluster_centers[cluster,])
    }
    classifications[point] = which.min(distances[point,])
  }
  # for (cluster in 1:1) {
  #   filter = classifications[] == cluster
  #   p = list(X[filter], Y[filter])
  #   xs = X[filter]
  #   ys = Y[filter]
  #   print(points[1,])
  #   print(p[[1]])
  #   print(cluster_centers[cluster,])
  #   print(distance(p[cluster], cluster_centers[cluster,]))
  # }
}
print(distances)
print(classifications)

