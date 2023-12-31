#!/usr/bin/python3

# This program is a benchmarking tool to help you understand your code's performance
# as you optimize for the benchmark competition.
# Because each computer's performance is different, you should not consider the
# results of this program to be directly applicable to your results.
# Before submitting your code for benchmarking, you should check that it provides
# accurate solutions by running contest_eligibility.py

import os
import timeit
import statistics
import sys


# You are encouraged to make optimizations that might invalidate autograder, but
# PLEASE make any such optimizations in a secondary file called a2_bonus.py
import a2

try:
    import a2_bonus
except ImportError: a2_bonus = None

modules = [a2]
if not a2_bonus is None: modules.append(a2_bonus)

# You can put extra imports here to consider additional versions of your solution.
#import a2_alt
#modules.append(a2_alt)


##test_dirs = []
##for name in os.listdir('tests/'):
##    if os.path.isdir('tests/'+name):
##        test_dirs.append(name)
##test_dirs.sort()

# Please do not be discouraged if your initial code cannot complete
# all of these tests. Perhaps only run it on the easier ones for now?
test_dirs = ['test-1-easy', 'test-2-medium', 'test-3-hard']
test_dirs.append('test-4-tough')
test_dirs.append('test-5-brutal')
test_dirs.append('test-6-ridiculous')


verbose = True    # gives times for every board solved. can get a little tedious.
use_timeout = True # stops solutions that take over 8 min to solve a board 4 times.


### Timeout code credit David Narayan, Thomas Ahle, and vmarquet
### https://stackoverflow.com/questions/2281850/timeout-function-if-it-takes-too-long-to-finish
### Timeout works on *nix only.

if sys.platform in ['linux', 'darwin'] and use_timeout:
    import signal

    class timeout:
        def __init__(self, error_code=None, seconds=480):
            self.seconds = seconds
            if error_code:
                self.error_code = error_code
            else:
                self.error_code = "Test exceeded timeout of %i seconds." % self.seconds
        def handle_timeout(self, signum, frame):
            raise TimeoutError(self.error_code)
        def __enter__(self):
            signal.signal(signal.SIGALRM, self.handle_timeout)
            signal.alarm(self.seconds)
        def __exit__(self, type, value, traceback):
            signal.alarm(0)
else:
    class timeout: # dummy timeout
        def __init__(self, error_code=None, seconds=480): pass
        def __enter__(self): pass
        def __exit__(self, type, value, traceback): pass


reps = 1

def timeme(module, path):
    return timeit.timeit('Solver().solveBoard(Board(path))', number=1,
        globals={'Solver': module.Solver, 'Board': module.Board, 'path': path})

for test_dir in test_dirs:
    print('\n=========================')
    print('    Test: '+test_dir)
    print('=========================')
    tests = os.listdir('tests/'+test_dir)
    tests.sort()

    for module in modules:
        print('    With: '+str(module))
        try:
            times = []
            for test in tests:
                if test.endswith('.csv'): # valid test
                    if len(tests) < 20 or verbose:
                        print('  testing '+test+'...')

                    with timeout('Timeout on board %s/%s' % (test_dir, test)):
                        t = 0
                        for _ in range(reps):
                            t += timeme(module, "tests/%s/%s"  % (test_dir, test))/reps*1000
                    times.append(t)

                    if len(tests) < 20 or verbose:
                        print('   %5ims' % int(t))
            print('\nTotal:   %5ims\n Min:    %5ims\n Max:    %5ims\n Mean:   %5ims\n Median: %5ims\n-------------------------' %(
              int(sum(times)),
              int(min(times)),
              int(max(times)),
              int(statistics.mean(times)),
              int(statistics.median(times))
            ))
        except TimeoutError as err:
            print('\nTest failed: %s\n-------------------------' % str(err))
