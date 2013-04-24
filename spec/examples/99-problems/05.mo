# P05 (*) Reverse a list.

use Test::More;

my @list = [1, 1, 2, 3, 5, 8];

is_deeply(@list.reverse, [8, 5, 3, 2, 1, 1], "... P05");

done_testing(1);

