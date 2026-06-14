#!/usr/bin/env python3
"""Regenerate WikiBountyFallback.java from the OSRS wiki bounty tasks page."""

import json
import re
import urllib.request

WIKI_API = (
    "https://oldschool.runescape.wiki/api.php"
    "?action=parse&page=Bounty_tasks&prop=wikitext&format=json"
)
OUTPUT = "src/main/java/com/finishedbountynotify/WikiBountyFallback.java"


def parse_line(raw: str) -> dict[str, str]:
    return {m.group(1): m.group(2) for m in re.finditer(r"(\w+)=([^|]+)", raw)}


def main() -> None:
    request = urllib.request.Request(WIKI_API, headers={"User-Agent": "FinishedBountyNotify/1.0"})
    with urllib.request.urlopen(request, timeout=60) as response:
        text = json.load(response)["parse"]["wikitext"]["*"]

    entries = [parse_line(line) for line in re.findall(r"\{\{BountyTaskLine\|([^}]+)\}\}", text)]
    entries.sort(key=lambda entry: int(entry["taskId"]))

    lines = [
        "package com.finishedbountynotify;",
        "",
        "import java.util.HashMap;",
        "import java.util.Map;",
        "",
        "/** Quantities from https://oldschool.runescape.wiki/w/Bounty_tasks (wiki fallback). */",
        "final class WikiBountyFallback",
        "{",
        "\tprivate static final Map<Integer, Integer> QUANTITY_BY_TASK_ID = new HashMap<>();",
        "\tprivate static final Map<Integer, String> MONSTER_BY_TASK_ID = new HashMap<>();",
        "",
        "\tstatic",
        "\t{",
    ]

    for entry in entries:
        task_id = entry["taskId"]
        qty = entry["qty"]
        monster = entry["monster"].replace('"', '\\"')
        lines.append(f"\t\tQUANTITY_BY_TASK_ID.put({task_id}, {qty});")
        lines.append(f'\t\tMONSTER_BY_TASK_ID.put({task_id}, "{monster}");')

    lines.extend(
        [
            "\t}",
            "",
            "\tprivate WikiBountyFallback() {}",
            "",
            "\tstatic int getQuantity(int taskId)",
            "\t{",
            "\t\treturn QUANTITY_BY_TASK_ID.getOrDefault(taskId, 0);",
            "\t}",
            "",
            "\tstatic String getMonsterName(int taskId)",
            "\t{",
            "\t\treturn MONSTER_BY_TASK_ID.get(taskId);",
            "\t}",
            "}",
            "",
        ]
    )

    with open(OUTPUT, "w", encoding="utf-8") as file:
        file.write("\n".join(lines))

    print(f"Wrote {len(entries)} entries to {OUTPUT}")


if __name__ == "__main__":
    main()
