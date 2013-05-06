# P26 (**) Generate the combinations of K distinct objects chosen from the N elements of a list.

#         In how many ways can a committee of 3 be chosen from a group
#         of 12 people? We all know that there are C(12,3) = 220
#         possibilities (C(N,K) denotes the well-known binomial
#         coefficient). For pure mathematicians, this result may be
#         great. But we want to really generate all the possibilities.

#       Example:

#       moe> combinations(3, ['a', 'b', 'c', 'd', 'e', 'f'])
#       [['a', 'b', 'c'], ['a', 'b', 'd'], ['a', 'b', 'e'], ...]

use Test::More;

sub concat(@a, @b) {
    @b.each(-> ($x) { @a.push($x) });
    @a
}

sub combinations($n, @list) {
    if ($n == 1) {
        @list.map(-> ($x) { [$x] });
    }
    elsif (@list.length <= $n) {
        [@list];
    }
    else {
        concat(
            combinations($n - 1, @list.tail)
                .map(
                    -> (@c) {
                        [@list.head, @c].flatten
                    }
                ),
            combinations($n, @list.tail)
        )
    }
}

ok(combinations(3, ['a', 'b', 'c', 'd', 'e', 'f'])
   .eqv([["a", "b", "c"], ["a", "b", "d"], ["a", "b", "e"], ["a", "b", "f"],
         ["a", "c", "d"], ["a", "c", "e"], ["a", "c", "f"],
         ["a", "d", "e"], ["a", "d", "f"],
         ["a", "e", "f"],
         ["b", "c", "d"], ["b", "c", "e"], ["b", "c", "f"],
         ["b", "d", "e"], ["b", "d", "f"],
         ["b", "e", "f"],
         ["c", "d", "e"], ["c", "d", "f"],
         ["c", "e", "f"],
         ["d", "e", "f"]]),
   "... P26");

done_testing();
