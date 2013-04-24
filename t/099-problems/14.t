# P14 (*) Duplicate the elements of a list.
#     Example:

#     moe> duplicate(["a", "b", "c", "c", "d"])
#     ["a", "a", "b", "b", "c", "c", "c", "c", "d", "d"]

use Test::More;

sub duplicate(@list) {
    @list.map(-> ($x) {[$x, $x]}).flatten
}

is_deeply(duplicate(["a", "b", "c", "c", "d"]),
          ["a", "a", "b", "b", "c", "c", "c", "c", "d", "d"],
          "... P14");

done_testing();

