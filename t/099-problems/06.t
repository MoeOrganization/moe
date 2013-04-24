# P06 (*) Find out whether a list is a palindrome.
#     Example:

#     moe> is_palindrome([1, 2, 3, 2, 1)]
#     true

use Test::More;

sub is_palindrome(@list) {
    @list.reverse.eqv(@list);
}

is(is_palindrome([1, 2, 3, 2, 1]), true,  "... P06 -- true") ;
is(is_palindrome([1, 2, 3, 4, 5]), false, "... P06 -- false") ;

done_testing(2);

