# P19 (**) Rotate a list N places to the left.
#     Examples:

#     moe> rotate(3, ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"])
#     ["d", "e", "f", "g", "h", "i", "j", "k", "a", "b", "c""]

#     moe> rotate(-2, ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"])
#     ["j", "k", "a", "b", "c", "d", "e", "f", "g", "h", "i"]

sub rotate($n, @list) {
    my $len = @list.length;
    my @r = @list[($n < 0 ? $len + $n : $n) .. ($len - 1)];
    @r.push(@list[0 .. ($n < 0 ? $len + $n : $n-1)]);
    @r.flatten
}

say rotate(3, ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"]);
say rotate(-2, ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"]);
