setwd("D:/Classes/B365/Data")
x = matrix(scan("tree_data.dat"),byrow=T,ncol = 3)


alpha = 0.03
total = x[1,1] + x[1,2]
x[10:17,3] = -1 #marking all "nonexistent" nodes
print(x)

length = length(x[,1])
optimal_risks = rep(1:length)
optimal_r_t = rep(1:length)
optimal_penalty_risks = rep(1:length)

for (i in 1:length) {
  optimal_risks[i] = get_optimal_risk(i)
  optimal_penalty_risks[i] = get_optimal_penalty_risk(i)
  optimal_r_t[i] = get_r_t(i)
}

print(optimal_risks)
print(optimal_r_t)
print(optimal_penalty_risks)

get_optimal_risk <- function(n) {
  if (n > 19)
    return(0)
  if (x[n,3] == 1) { #node is terminal (base case)
    if (x[n,1] < x[n,2])
      return(x[n,1] / total)
    else
      return(x[n,2] / total)
  }
  else if (x[n,3] == 0) { #node has children (recursive case)
    r_alphastar = get_optimal_risk(get_left_child(n)) + get_optimal_risk(get_right_child(n)) + alpha
    r_t = x[n,(which.max(c(x[n,1], x[n,2])))] / total
    
    if (r_alphastar > r_t)
      return(r_alphastar)
    else
      return(r_t)
  }
  else {
    return(0)
  }
}
get_optimal_penalty_risk <- function(n) {
  if (x[n,3] == 1) { #node is terminal (base case)
    if (x[n,1] < x[n,2])
      return(x[n,1] / total)
    else
      return(x[n,2] / total)
  }
  else if (x[n,3] == 0) { #node has children (recursive case)
    r_alphastar = get_optimal_risk(get_left_child(n)) + get_optimal_risk(get_right_child(n)) + alpha
    return(r_alphastar)
  }
  else { #super duper base case. Should never be used
    return(0)
  }
}
get_r_t <- function(n) {
  if (x[n,3] == 1) { #node is terminal (base case)
    if (x[n,1] < x[n,2])
      return(x[n,1] / total)
    else
      return(x[n,2] / total)
  }
  else if (x[n,3] == 0) { #node has children (recursive case)
    r_t = get_r_t(get_left_child(n)) + get_r_t(get_right_child(n))
    return(r_t)
  }
  else {
    return(0)
  }
}

get_left_child <- function(i) {
  return(2 * i)
}
get_right_child <- function(i) {
  return((2* i) + 1)
}