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

bag_a <- function(num_rolls) {
  return(sample(1:2, num_rolls, replace=TRUE, prob=probabilities[1,]))
}
bag_b <- function(num_rolls) {
  return(sample(1:2, num_rolls, replace=TRUE, prob=probabilities[2,]))
}
bag_c <- function(num_rolls) {
  return(sample(1:2, num_rolls, replace=TRUE, prob=probabilities[3,]))
}
bag_d <- function(num_rolls) {
  return(sample(1:2, num_rolls, replace=TRUE, prob=probabilities[4,]))
}

choose_bags <- function() {
  return(sample(c("A", "B", "C", "D"), 1, replace = TRUE, prob = p_bags))
}

candies = 5
result = choose_bags()

candies = switch(result, "A" = bag_a(candies), "B" = bag_b(candies), "C" = bag_c(candies), "D" = bag_d(candies))

print(result)
print(candies)