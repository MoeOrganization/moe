# P16 (**) Drop every Nth element from a list.
#     Example:

#     moe> drop(3, ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"])
#     ["a", "b", "d", "e", "g", "h", "j", "k"]

use Test::More;

sub drop($n, @list) {
    # need prefix -- (pre-decrement) operator for this to work
    # @list.grep(($a) => { --$n % 3 != 0 })

    # until then:
    @list.grep(($a) => { $n = $n - 1; $n % 3 != 0 })
}

is_deeply(drop(3, ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"]),
          ["a", "b", "d", "e", "g", "h", "j", "k"],
          "... P16");

done_testing();

