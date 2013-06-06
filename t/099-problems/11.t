# P11 (*) Modified run-length encoding.
#     Modify the result of problem P10 in such a way that if an
#     element has no duplicates it is simply copied into the result
#     list. Only elements with duplicates are transferred as (N, E)
#     terms.

#     Example:

#     moe> encodeModified("a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e")
#     [[4, "a"], "b", [2, "c"], [2, "a"], "d", [4, "e"]]

use Test::More;

sub encodeModified(*@list) {
    @list.reduce((@a, $b) => {
                     if (@a.length == 0 || @a[-1].at_pos(0) ne $b) {
                         @a.push([$b]);
                     }
                     else {
                         @a[-1].push($b);
                     }
                     @a
                 },
                 [])
         .map((@a) => {@a.length == 1 ? @a[0] : [@a.length, @a[0]]})
}

is_deeply(encodeModified('a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e'),
          [[4, "a"], "b", [2, "c"], [2, "a"], "d", [4, "e"]],
          "... P11");

done_testing();

