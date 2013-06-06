# P10 (*) Run-length encoding of a list.
#     Use the result of problem P09 to implement the so-called
#     run-length encoding data compression method. Consecutive
#     duplicates of elements are encoded as lists (N E) where N is the
#     number of duplicates of the element E.

#     Example:
#     moe> encode(["a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e"])
#     [[4, "a"], [1, "b"], [2, "c"], [2, "a"], [1, "d"], [4, "e"]]

use Test::More;

sub pack(@list) {
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
}

sub encode(*@list) {
    pack(@list).map((@a) => {[@a.length, @a[0]]})
}

is_deeply(encode("a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e"),
          [[4, "a"], [1, "b"], [2, "c"], [2, "a"], [1, "d"], [4, "e"]],
          "... P10");

done_testing();

