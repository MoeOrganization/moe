# P18 (**) Extract a slice from a list.
#     Given two indices, I and K, the slice is the list containing the
#     elements from and including the Ith element up to but not
#     including the Kth element of the original list. Start counting
#     the elements with 0.

#     Example:

#     moe> slice(3, 7, ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"])
#     ["d", "e", "f", "g"]

sub slice_list($s, $e, @list) {
    # XXXX: the range operand without enclosing parens should work too!
    @list[$s .. ($e-1)]
}
say slice_list(3, 7, ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"]);
