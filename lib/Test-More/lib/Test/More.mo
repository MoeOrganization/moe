package Test::More {

    my $test_count = 0;

    sub plan ($count) is export {
        say(1, "..", $count);
    }

    sub done_testing is export {
        say(1, "..", $test_count)
    }

    sub ok ($test, $msg?) is export {
        $test_count = $test_count + 1;
        if ($test) {
            # NOTE:
            # the "".pad(1) silliness is because 
            # the parser (for some reason) does 
            # not like strings with just spaces
            # and instead gives back an empty string
            # and I can't figure it out.
            # - SL
            say([ "ok", $test_count, ($msg || "") ].join("".pad(1))); 
        } else {
            say([ "not ok", $test_count, ($msg || "") ].join("".pad(1)))
        }
    }

    sub is ($got, $expected, $msg?) is export {
        $test_count = $test_count + 1;
        if (~$got eq ~$expected) {
            # NOTE:
            # the "".pad(1) silliness is because 
            # the parser (for some reason) does 
            # not like strings with just spaces
            # and instead gives back an empty string
            # and I can't figure it out.
            # - SL
            say([ "ok", $test_count, ($msg || "") ].join("".pad(1))); 
        } else {
            say([ "not ok", $test_count, ($msg || "") ].join("".pad(1)))
        }
    }

}
