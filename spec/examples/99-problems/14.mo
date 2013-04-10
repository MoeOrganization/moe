# P14 (*) Duplicate the elements of a list.
#     Example:

#     moe> duplicate(["a", "b", "c", "c", "d"])
#     ["a", "a", "b", "b", "c", "c", "c", "c", "d", "d"]

sub duplicate(@list) {
    @list.map(-> ($x) {[$x, $x]}).flatten
}

say duplicate(["a", "b", "c", "c", "d"]);
