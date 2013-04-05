
## http://www.scala-lang.org/node/242

const PING => 'ping';
const PONG => 'pong';
const STOP => 'stop';

class Ping extends Actor {
    has $.count;
    has $.pong;

    BUILD ($count, $pong) {
        $.count = $count;
        $.pong  = $pong;
    }

    method act {
        my $pings_left = $.count - 1;
        $.pong->send(PING);
        while (true) {
            recieve ($sender) {
                when (PONG) {
                    if ($pings_left % 1000 == 0) {
                        say("Ping: pong")
                    } elsif ($pings_left > 0) {
                        $.pong->send(PING);
                        $pings_left = $pings_left - 1; 
                    } else {
                        say("Ping: stop");
                        $.pong->send(STOP);
                        $self->exit();
                    }
                }
            }
        }
    }
}

class Pong extends Actor {
    method act {
        my $pong_count = 0;
        while (true) {
            recieve ($sender) {
                when (PING) {
                    if ($pong_count % 1000 == 0) {
                        say("Pong: ping " . $pong_count);
                    }
                    $sender->send(PONG);
                    $pong_count = $pong_count + 1;
                }
                when (STOP) {
                    say("Pong: stop");
                    $self->exit();
                }
            }
        }
    }
}

sub main (*@ARGV) {
    my $pong = ^Pong->new;
    my $ping = ^Ping->new(100_000, $pong);
    $ping->start;
    $pong->start;
}

