# P07 (**) Flatten a nested list structure.

use Test::More;

my @list = [[1, 1], 2, [3, [5, 8]]];

is_deeply(@list.flatten, [1, 1, 2, 3, 5, 8], "... P07");

done_testing(1);

