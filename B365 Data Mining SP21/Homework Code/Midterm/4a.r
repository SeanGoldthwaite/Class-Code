data(iris3)   # (different form of same data) 3 dim array,  iris3[50 examples, 4 attributes, 3 classes]
#print(iris)

bk = c(0,.5,1.,1.5,2.,8)  # the breakpoints we will used in quantizing the petal_len variable
values = length(bk)-1;

species = 3

sepall = 1
sepalw = 2
petall = 3
petalw = 4

iris3[,c(petall,petalw),] = cut(iris3[,c(petall,petalw),],breaks=bk,labels=1:values)
x = iris3[,c(petall,petalw),]
#print(x)

q = array(0.0,dim=c(values,values,species), dimnames = list(c(1,2,3,4,5), c(1,2,3,4,5), c("Setosa", "Veriscolor", "Virginica")))

len = length(x[,1,1])

for (spec in 1:species) {
  for (petall in 1:values) {
    for (petalw in 1:values) {
      q[petall,petalw,spec] = sum(x[,1,spec] == petall & x[,2,spec] == petalw)/50
    }
  }
}
print(q)