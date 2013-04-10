# P06 (*) Find out whether a list is a palindrome.
#     Example:

#     moe> is_palindrome([1, 2, 3, 2, 1)]
#     true

sub is_palindrome(@list) {
    @list.reverse.eqv(@list);
}

my @list = [1, 2, 3, 2, 1];
say is_palindrome(@list);
