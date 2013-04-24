# P17 (*) Split a list into two parts.
#     The length of the first part is given.

#     Example:

#     moe> split_list(3, ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"])
#     [["a", "b", "c"], ["d", "e", "f", "g", "h", "i", "j", "k"]]

sub split_list($n, @list) {
    # XXXX: the range expressions should work without enclosing parens too!
    [@list[0..($n-1)], @list[$n..(@list.length-1)]]
}

say split_list(3, ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"]);

