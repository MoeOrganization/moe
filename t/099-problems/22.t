# P22 (*) Create a list containing all integers within a given range.
#     Example:

#     moe> range(4, 9)
#     [4, 5, 6, 7, 8, 9]

use Test::More;

sub range($start, $end) { $start .. $end }

is_deeply(range(4, 9), [4, 5, 6, 7, 8, 9], "... P22");

done_testing(1);

