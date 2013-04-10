# P09 (**) Pack consecutive duplicates of list elements into sublists.
#     If a list contains repeated elements they should be placed in
#     separate sublists.

#     Example:
#     moe> pack(['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e')]
#     [['a', 'a', 'a', 'a']', ['b']', ['c', 'c']', ['a', 'a']', ['d']', ['e', 'e', 'e', 'e']]

sub pack(*@list) {
    @list.reduce(-> (@a, $b) {
                     if (@a.length == 0 || @a[-1].at_pos(0) ne $b) {
                         @a.push([$b]);
                     }
                     else {
                         @a[-1].push($b);
                     }
                     @a
                 },
                 [])
}

say pack('a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e');
