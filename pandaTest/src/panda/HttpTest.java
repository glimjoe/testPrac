package panda;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.msgpack.core.ExtensionTypeHeader;
import org.msgpack.core.MessageFormat;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.ValueType;

import java.io.IOException;
import java.io.InputStream;

public class HttpTest {

//    public static void main(String[] args){
//        downLoad("http://cdn.data.fate-go.jp/AssetStorages/Bf95U3NV/Android/48261f006e476cc0ad56457380876aa73a3cb21c.bin?v=FgoDataVersion0066_20160420_1_302080_2834209670", "E:\\48261f006e476cc0ad56457380876aa73a3cb21c.bin");
//    }

    public static void main(String[] args) {
        HttpClient httpClient = HttpClients.createDefault();
//        OutputStream out = null;
        InputStream in = null;

        try {
            HttpGet httpGet = new HttpGet("http://cdn.data.fate-go.jp/AssetStorages/Bf95U3NV/Android/48261f006e476cc0ad56457380876aa73a3cb21c.bin?v=FgoDataVersion0066_20160420_1_302080_2834209670");
//            httpGet.addHeader("fileName", remoteFileName);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            in = entity.getContent();

            long length1 = entity.getContentLength();
            if (length1 <= 0) {
                System.out.println("下载文件不存在！");
                return;
            }

            System.out.println("The response value of token:" + httpResponse.getFirstHeader("token"));

//            File file = new File(localFileName);
//            if(!file.exists()){
//                file.createNewFile();
//            }

//            out = new FileOutputStream(file);
            byte[] bytes = IOUtils.toByteArray(in);
            System.out.println(bytes);
            MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(bytes);
//            ExtensionTypeHeader et = unpacker.unpackExtensionTypeHeader();
            String result = readRecursively(unpacker);
            System.out.println(result);
//            System.out.println(temp);
//            byte[] buffer = new byte[4096];
//            int readLength = 0;
//            while ((readLength=in.read(buffer)) > 0) {
//                byte[] bytes = new byte[readLength];
//                System.arraycopy(buffer, 0, bytes, 0, readLength);
//                out.write(bytes);
//            }

//            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readRecursively(MessageUnpacker unpacker){
        StringBuffer sb = new StringBuffer();
        try {
            while (unpacker.hasNext()) {
                MessageFormat format = unpacker.getNextFormat();
                ValueType type = format.getValueType();
                int length;
                ExtensionTypeHeader extension;
                switch (type) {
                    case NIL:
                        unpacker.unpackNil();
                        break;
                    case BOOLEAN:
                        sb.append(unpacker.unpackBoolean());
                        break;
                    case INTEGER:
                        switch (format) {
                            case UINT64:
                                sb.append(unpacker.unpackBigInteger());
                                break;
                            case INT64:
                            case UINT32:
                                sb.append(unpacker.unpackLong());
                                break;
                            default:
                                sb.append(unpacker.unpackInt());
                                break;
                        }
                        break;
                    case FLOAT:
                        sb.append(unpacker.unpackDouble());
                        break;
                    case STRING:
                        sb.append(unpacker.unpackString());
                        break;
                    case BINARY:
                        length = unpacker.unpackBinaryHeader();
                        unpacker.readPayload(new byte[length]);
                        break;
                    case ARRAY:
                        length = unpacker.unpackArrayHeader();
                        for (int i = 0; i < length; i++) {
                            readRecursively(unpacker);
                        }
                        break;
                    case MAP:
                        length = unpacker.unpackMapHeader();
                        for (int i = 0; i < length; i++) {
                            readRecursively(unpacker);  // key
                            readRecursively(unpacker);  // value
                        }
                        break;
                    case EXTENSION:
                        extension = unpacker.unpackExtensionTypeHeader();
                        unpacker.readPayload(new byte[extension.getLength()]);
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
