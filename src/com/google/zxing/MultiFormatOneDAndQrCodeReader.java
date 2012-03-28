package com.google.zxing;

import com.google.zxing.oned.MultiFormatOneDReader;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class MultiFormatOneDAndQrCodeReader implements Reader {

    private Map<DecodeHintType,?> hints;
    private Reader[] readers;


    @Override
    public Result decode(BinaryBitmap image) throws NotFoundException {
        setHints(null);
        return decodeInternal(image);
    }

    @Override
    public Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints) throws NotFoundException {
        setHints(hints);
        return decodeInternal(image);
    }

    public Result decodeWithState(BinaryBitmap image) throws NotFoundException {
        // Make sure to set up the default state so we don't crash
        if (readers == null) {
            setHints(null);
        }
        return decodeInternal(image);
    }

    public void setHints(Map<DecodeHintType,?> hints) {
        this.hints = hints;

        boolean tryHarder = hints != null && hints.containsKey(DecodeHintType.TRY_HARDER);
        Collection<BarcodeFormat> formats =
            hints == null ? null : (Collection<BarcodeFormat>) hints.get(DecodeHintType.POSSIBLE_FORMATS);
        Collection<Reader> readers = new ArrayList<Reader>();
        if (formats != null) {
            boolean addOneDReader =
                formats.contains(BarcodeFormat.UPC_A) ||
                formats.contains(BarcodeFormat.UPC_E) ||
                formats.contains(BarcodeFormat.EAN_13) ||
                formats.contains(BarcodeFormat.EAN_8) ||
                //formats.contains(BarcodeFormat.CODABAR) ||
                formats.contains(BarcodeFormat.CODE_39) ||
                formats.contains(BarcodeFormat.CODE_93) ||
                formats.contains(BarcodeFormat.CODE_128) ||
                formats.contains(BarcodeFormat.ITF) ||
                formats.contains(BarcodeFormat.RSS_14) ||
                formats.contains(BarcodeFormat.RSS_EXPANDED);
            // Put 1D readers upfront in "normal" mode
            if (addOneDReader && !tryHarder) {
                readers.add(new MultiFormatOneDReader(hints));
            }
            if (formats.contains(BarcodeFormat.QR_CODE)) {
                readers.add(new QRCodeReader());
            }
            // At end in "try harder" mode
            if (addOneDReader && tryHarder) {
                readers.add(new MultiFormatOneDReader(hints));
            }
        }
        if (readers.isEmpty()) {
            if (!tryHarder) {
                readers.add(new MultiFormatOneDReader(hints));
            }

            readers.add(new QRCodeReader());

            if (tryHarder) {
                readers.add(new MultiFormatOneDReader(hints));
            }
        }
        this.readers = readers.toArray(new Reader[readers.size()]);
    }

    @Override
    public void reset() {
        if (readers != null) {
            for (Reader reader : readers) {
                reader.reset();
            }
        }
    }

    private Result decodeInternal(BinaryBitmap image) throws NotFoundException {
        if (readers != null) {
            for (Reader reader : readers) {
                try {
                    return reader.decode(image, hints);
                } catch (ReaderException re) {
                    // continue
                }
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

}
