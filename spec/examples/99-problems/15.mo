# P15 (**) Duplicate the elements of a list a given number of times.
#     Example:

#     moe> duplicateN(3, ["a", "b", "c", "c", "d"])
#     ["a", "a", "a", "b", "b", "b", "c", "c", "c", "c", "c", "c", "d", "d", "d"]

sub duplicateN($n, @list) {
    @list.map(-> ($x) {[$x] x $n}).flatten
}

say duplicateN(3, ["a", "b", "c", "c", "d"]);
