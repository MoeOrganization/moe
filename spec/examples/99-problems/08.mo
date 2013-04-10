# P08 (**) Eliminate consecutive duplicates of list elements.
#     If a list contains repeated elements they should be replaced
#     with a single copy of the element. The order of the elements
#     should not be changed.

#     Example:

#     moe> compress(['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e'])
#     ['a', 'b', 'c', 'a', 'd', 'e']

sub compress(*@list) {
    @list.reduce(-> (@a, $b) {
                     if (@a.length == 0 || @a[-1] ne $b) {
                         @a.push($b)
                     }
                     @a},
                 [])
}

say compress('a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e');
