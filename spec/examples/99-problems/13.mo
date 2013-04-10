# P13 (**) Run-length encoding of a list (direct solution).
#     Implement the so-called run-length encoding data compression
#     method directly. I.e. don't use other methods you've written
#     (like P09's pack); do all the work directly.

#     Example:
#     moe> encodeDirect(["a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e"])
#     [[4, "a"], [1, "b"], [2, "c"], [2, "a"], [1, "d"], [4, "e"]]

sub encodeDirect(*@list) {
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
            .map(-> (@a) {[@a.length, @a[0]]})
}

say encodeDirect("a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e");
