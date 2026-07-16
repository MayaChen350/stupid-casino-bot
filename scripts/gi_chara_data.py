import re, os

with open("gi_data.txt", "r") as file:
    content: str = file.read()

# Data is coming from the "Playable Characters" table at https://genshin-impact.fandom.com/wiki/Character/List
# Copied and pasted into an external file (scripts/gi_data.txt)
regex = r"(?P<name>.+?) Icon \t(?P=name) \t(?P<stars>4|5) Stars \tElement (?P<element>\w+) (?P=element) \tWeapon-class-[a-z]+-icon (?P<weapon>\w+).*?(?P<region>\S+) \t\w+ (?P<gender>F|M)"

matches_iter = re.finditer(regex, content)

os.makedirs("../src/mayachen350/casinobot/gambling/gen_data", exist_ok=True)
with open("../src/mayachen350/casinobot/gambling/gen_data/gi_data.clj", "w") as file:
    _ = file.write(
        ";; GENERATED, DO NOT MODIFY!!\n"
        + "(ns mayachen350.casinobot.gambling.gen-data.gi-data)\n\n"
        + "(def gi-chara-data ["
    )

    for match in matches_iter:
        name, stars, element, weapon, region, gender = match.groupdict().values()

        _ = file.write(
            '\n  ["'
            + name
            + '" '
            + stars
            + " :"
            + gender.lower()
            + " :"
            + element.lower()
            + " :"
            + weapon.lower()
            + " :"
            + region.lower()
            + "]"
        )

    # add missing characters (format too different to be included in the regex)
    _ = file.write(
        '\n  ["Lumine" 5 :f :anemo :geo :electro :dendro :hydro :pyro :cryo :sword]'
        + '\n  ["Aether" 5 :m :anemo :geo :electro :dendro :hydro :pyro :cryo :sword]'
        + '\n  ["Manekin" 5 :m :anemo :geo :electro :dendro :hydro :pyro :cryo :sword]'
        + '\n  ["Manekina" 5 :f :anemo :geo :electro :dendro :hydro :pyro :cryo :sword]'
        + "])"
    )
