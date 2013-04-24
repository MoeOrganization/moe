# P02 (*) Find the last but one element of a list.

use Test::More;

my @list = [1, 1, 2, 3, 5, 8];

is(@list[-2], 5, "... P02");

done_testing(1);
