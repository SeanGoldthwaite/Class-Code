p_bags = c(.1, .4, .3, .2)

probabilities = matrix(nrow = 4, ncol = 2, dimnames=list(c("Bag A", "Bag B", "Bag C", "Bag D"),c("Apple", "Cherry")))
#bag a
probabilities[1,1] = 1
probabilities[1,2] = 0

#bag b
probabilities[2,1] = .75
probabilities[2,2] = .25

#bag c
probabilities[3,1] = .25
probabilities[3,2] = .75

#bag d
probabilities[4,1] = 0
probabilities[4,2] = 1
print(probabilities)

candies = c(1, 1, 1, 1, 1)

print(candies)

p_result_bag = matrix(nrow = 1, ncol = 4, dimnames=list(c("Result"), c("A", "B", "C", "D")))

for (bag in 1:ncol(p_result_bag)) {
  prob = 1
  for (candy in bag:length(candies)) {
    prob = prob * probabilities[bag, candies[candy]]
  }
  p_result_bag[bag] = prob
}
p_result = sum(p_result_bag * p_bags)
  
print(p_result_bag)
print((p_result_bag * p_bags) / p_result)
print(p_result_bag * p_bags)