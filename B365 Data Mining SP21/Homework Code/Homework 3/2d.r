pa = 0.10
pb = 0.40
pc = 0.50

pfa = 0.40
pfb = 0.60
pfc = 0.80

n = 10000

#vector representing all 10,000 individuals
res = runif(n, 0, 1) < pa

sprintf("There are %g total members of party A", sum(res))

for (i in 1:n) {
  if (res[i] == 1) {
    res[i] = runif(1) < pfa
  }
}
  
sprintf("The number of individuals in party A who favor the measure is %g", sum(res))