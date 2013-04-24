# P01 (*) Find the last element of a list.

use Test::More;

my @list = [1, 1, 2, 3, 5, 8];

is(@list[-1], 8, "... P01");

done_testing(1);
