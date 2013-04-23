# P22 (*) Create a list containing all integers within a given range.
#     Example:

#     moe> range(4, 9)
#     [4, 5, 6, 7, 8, 9]

sub range($start, $end) { $start .. $end }

say range(4, 9)

