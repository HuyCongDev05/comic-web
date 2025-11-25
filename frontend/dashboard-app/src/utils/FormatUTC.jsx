export function FormatUTC(isoString) {
    const date = new Date(isoString);

    const pad = (n) => n.toString().padStart(2, "0");

    const hours = pad(date.getUTCHours());
    const minutes = pad(date.getUTCMinutes());
    const seconds = pad(date.getUTCSeconds());

    const day = pad(date.getUTCDate());
    const month = pad(date.getUTCMonth() + 1);
    const year = date.getUTCFullYear();

    return `${hours}:${minutes}:${seconds} ${day}-${month}-${year} (UTC)`;
}
