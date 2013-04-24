# P15 (**) Duplicate the elements of a list a given number of times.
#     Example:

#     moe> duplicateN(3, ["a", "b", "c", "c", "d"])
#     ["a", "a", "a", "b", "b", "b", "c", "c", "c", "c", "c", "c", "d", "d", "d"]

use Test::More;

sub duplicateN($n, @list) {
    @list.map(-> ($x) {[$x] x $n}).flatten
}

is_deeply(duplicateN(3, ["a", "b", "c", "c", "d"]),
          ["a", "a", "a", "b", "b", "b", "c", "c", "c", "c", "c", "c", "d", "d", "d"],
          "... P15");

done_testing(1);

