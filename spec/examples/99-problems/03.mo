# P03 (*) Find the Kth element of a list.
#     By convention, the first element in the list is element 0.

#     Example:

#     moe> nth(2, [1, 1, 2, 3, 5, 8)]
#     2

sub nth($n, @list) { @list[$n] }

say nth(2, [1, 1, 2, 3, 5, 8]);
