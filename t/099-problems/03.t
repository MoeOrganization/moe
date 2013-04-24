# P03 (*) Find the Kth element of a list.
#     By convention, the first element in the list is element 0.

#     Example:

#     moe> nth(2, [1, 1, 2, 3, 5, 8)]
#     2

use Test::More;

sub nth($n, @list) { @list[$n] }

is (nth(2, [1, 1, 2, 3, 5, 8]), 2, "... P03";

done_testing(1);
