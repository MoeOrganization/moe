# P12 (**) Decode a run-length encoded list.
#     Given a run-length code list generated as specified in problem
#     P10, construct its uncompressed version.

#     Example:

#     moe> decode([[4, "a"], [1, "b"], [2, "c"], [2, "a"], [1, "d"], [4, "e"]])
#     ["a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e"]

use Test::More;

sub decode(@list) {
    @list.map(-> (@rlc) {[@rlc[1]] x @rlc[0]}).flatten
}

is_deeply(decode([[4, "a"], [1, "b"], [2, "c"], [2, "a"], [1, "d"], [4, "e"]]),
          ["a", "a", "a", "a", "b", "c", "c", "a", "a", "d", "e", "e", "e", "e"],
          "... P12");

done_testing();

