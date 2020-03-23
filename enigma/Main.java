package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ria Vora
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        checkFirstSetting();
        while (_input.hasNext("[\\*]")) {
            setUp(m, checkNextLine());
        }
        checkRemainingLines();
    }

    /** Checks if there is a proper first setting in the input. */
    private void checkFirstSetting() {
        if (!_input.hasNext("[\\*]")) {
            throw new EnigmaException("Your input "
                    + "does not start with a *, "
                    + "and is not formatted correctly");
        }
    }

    /** Checks whether the next line is a space or a setting.
     * @return a String of the next line that is not a space*/
    private String checkNextLine() {
        String line = _input.nextLine();
        while (line.length() == 0) {
            line = _input.nextLine();
            _output.println();
        }
        return line;
    }

    /** Checks whether additional lines need to be added
     * to output. */
    private void checkRemainingLines() {
        while (_input.hasNextLine()) {
            String line = _input.nextLine();
            if (line.length() == 0 || line.matches("[ ]*")) {
                _output.println();
            } else {
                throw new EnigmaException("Your format"
                        + " does not match properly! "
                        + "The line " + line + " is not correct");
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();

            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }

            return new Machine(_alphabet, numRotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Creates a new Rotor based off of a name, type, and cycles.
     * @return a new Rotor based off the specifications
     * @param name is the name of the new Rotor
     * @param type is the type and notches (if needed) of the new Rotor
     * @param cycles is the cycles of the new Rotor*/
    private Rotor createNewRotor(String name, String type, String cycles) {
        EnigmaException exception = new EnigmaException("The rotor of name "
                + name + " " + " and cycles " + cycles
                + " has the incorrect type of " + type);
        Permutation perm = new Permutation(cycles, _alphabet);
        if (type.length() == 1) {
            if (type.equals("N")) {
                return new FixedRotor(name, perm);
            } else if (type.equals("R")) {
                return new Reflector(name, perm);
            } else if (type.equals("M")) {
                return new MovingRotor(name, perm, "");
            } else {
                throw exception;
            }
        } else if (type.length() > 1 && type.charAt(0) == 'M') {
            return new MovingRotor(name, perm, type.substring(1));
        } else {
            throw exception;
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String type = _config.next();
            String cycles = "";
            while (_config.hasNext("(\\([^ *\\(\\)]+\\))+")) {
                cycles += (_config.next() + " ");
            }
            return createNewRotor(name, type, cycles);

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        if (!(settings.charAt(0) == '*')) {
            throw new EnigmaException("You did not "
                    + "start your input with a *!");
        }
        settings = settings.replaceFirst("\\* +", "");
        String[] remaining = settings.split(" +");
        String[] rotors = new String[M.numRotors()];
        String cycles = "";
        for (int i = 0; i < remaining.length; i++) {
            if (i < M.numRotors()) {
                if (!M.hasRotor(remaining[i])) {
                    throw new EnigmaException(remaining[i]
                            + " is not a rotor that you "
                            + "passed in to all Rotors!");
                }
                rotors[i] = remaining[i];
            } else if (i == M.numRotors()) {
                if (M.hasRotor(remaining[i])) {
                    throw new EnigmaException("That is too many"
                            + " rotors! You are only "
                            + "supposed to have " + M.numRotors() + " rotors");
                }
                checkRotors(rotors);
                M.insertRotors(rotors);
                M.setRotors(remaining[i]);
            } else if (i > M.numRotors()
                    && remaining[i].matches(
                            "\\([^ *\\(\\)]+\\)")) {
                cycles += (remaining[i] + " ");
            }
        }

        ArrayList<String> messages = new ArrayList<String>();

        M.setPlugboard(new Permutation(cycles, _alphabet));

        while (_input.hasNext("[^\\*]+")) {
            messages.add(_input.nextLine());
        }

        for (int i = 0; i < messages.size(); i++) {
            String msg = messages.get(i);
            msg = msg.replaceAll("[ ]+", "");
            msg = M.convert(msg);
            printMessageLine(msg);
        }

    }

    /** Checks whether the rotors are of the correct number
     * and of the correct format.
     * @param rotors holds the names of each of the rotors*/
    private void checkRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            if (rotors[i] == null) {
                throw new EnigmaException("You do not have "
                        + "the correct numnber of rotors!");
            } else if (!rotors[i].matches("[^ \\*\\(\\)]+")) {
                throw new EnigmaException("Your input for the name "
                        + " of the rotor is " + _input.next()
                        + ", which is not allowed!");
            }
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        while (!msg.isEmpty() || !msg.matches(" *")) {
            int length = msg.length();
            while (length > 0) {
                if (length >= 5) {
                    length = 5;
                }
                String result = msg.substring(0, length);
                _output.print(result + " ");
                msg = msg.substring(length);
                length = msg.length();
            }
        }
        _output.println();
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
